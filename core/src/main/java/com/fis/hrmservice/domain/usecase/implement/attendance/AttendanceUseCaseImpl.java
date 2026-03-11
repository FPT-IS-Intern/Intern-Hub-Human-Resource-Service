package com.fis.hrmservice.domain.usecase.implement.attendance;

import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.model.attendance.AttendanceStatusModel;
import com.fis.hrmservice.domain.model.constant.AttendanceError;
import com.fis.hrmservice.domain.model.constant.AttendanceStatus;
import com.fis.hrmservice.domain.model.constant.CoreConstant;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.attendance.AttendanceRepositoryPort;
import com.fis.hrmservice.domain.port.output.network.NetworkCheckPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.fis.hrmservice.domain.usecase.attendance.AttendanceUseCase;
import com.fis.hrmservice.domain.usecase.command.attendance.AttendanceInWeekCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckInCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckOutCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.FilterAttendanceCommand;
import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.library.common.utils.Snowflake;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class AttendanceUseCaseImpl implements AttendanceUseCase {

    @Autowired
    private AttendanceRepositoryPort attendanceRepository;
    @Autowired
    private UserRepositoryPort userRepository;
    @Autowired
    private NetworkCheckPort networkCheckPort;
    @Autowired
    private Snowflake snowflake;

    @Value("${TIMEZONE_CONFIG:Asia/Ho_Chi_Minh}")
    private String timezoneConfig;

    private static final int STANDARD_CHECK_IN_HOUR = 8;
    private static final int STANDARD_CHECK_IN_MINUTE = 45;
    private static final int STANDARD_CHECK_OUT_HOUR = 17;
    private static final int STANDARD_CHECK_OUT_MINUTE = 15;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Get attendance status for a user on a specific date
     */
    @Override
    @Transactional(readOnly = true)
    public AttendanceStatusModel getAttendanceStatus(
            Long userId, LocalDate workDate, String clientIp, Double latitude, Double longitude) {
        log.info("Getting attendance status for userId: {} on date: {}", userId, workDate);

        Optional<AttendanceLogModel> openSessionOpt =
                attendanceRepository.findOpenSessionByUserAndDate(userId, workDate);
        Optional<AttendanceLogModel> latestOpt =
                attendanceRepository.findLatestByUserAndDate(userId, workDate);
        UUID currentBranchId = resolveCurrentBranchId(clientIp, latitude, longitude);

        if (openSessionOpt.isEmpty() && latestOpt.isEmpty()) {
            return AttendanceStatusModel.builder()
                    .workDate(workDate)
                    .checkInTime(null)
                    .checkOutTime(null)
                    .isCheckInValid(false)
                    .isCheckOutValid(false)
                    .checkInMessage(null)
                    .checkOutMessage(null)
                    .canCheckIn(true)
                    .canCheckOut(false)
                    .sessionOpen(false)
                    .openSessionBranchId(null)
                    .currentBranchId(currentBranchId)
                    .canResetByBranchChange(false)
                    .statusMessage(null)
                    .build();
        }

        AttendanceLogModel attendance = openSessionOpt.orElseGet(latestOpt::get);

        LocalTime checkInTime =
                attendance.getCheckInTime() > 0 ? convertToLocalTime(attendance.getCheckInTime()) : null;
        LocalTime checkOutTime =
                attendance.getCheckOutTime() > 0 ? convertToLocalTime(attendance.getCheckOutTime()) : null;

        boolean isCheckInValid =
                checkInTime != null
                        && !checkInTime.isAfter(LocalTime.of(STANDARD_CHECK_IN_HOUR, STANDARD_CHECK_IN_MINUTE));
        boolean isCheckOutValid =
                checkOutTime != null
                        && !checkOutTime.isBefore(
                        LocalTime.of(STANDARD_CHECK_OUT_HOUR, STANDARD_CHECK_OUT_MINUTE));

        boolean isSessionOpen = openSessionOpt.isPresent();
        UUID openSessionBranchId = isSessionOpen ? openSessionOpt.get().getCheckInBranchId() : null;
        boolean hasCompletedSession = attendance.getCheckInTime() > 0 && attendance.getCheckOutTime() > 0;
        UUID lastCompletedBranchId =
                attendance.getCheckOutBranchId() != null
                        ? attendance.getCheckOutBranchId()
                        : attendance.getCheckInBranchId();
        boolean canResetByBranchChange =
                hasCompletedSession
                        && currentBranchId != null
                        && lastCompletedBranchId != null
                        && !currentBranchId.equals(lastCompletedBranchId);
        boolean canCheckIn = !isSessionOpen && (!hasCompletedSession || canResetByBranchChange);
        String statusMessage = null;
        if (isSessionOpen) {
            String branchName =
                    networkCheckPort
                            .resolveBranchName(openSessionBranchId)
                            .orElse(openSessionBranchId != null ? openSessionBranchId.toString() : "khác");
            statusMessage = String.format("Bạn đã checkin Onsite ở %s", branchName);
        } else if (hasCompletedSession && !canResetByBranchChange) {
            statusMessage = "Bạn đã hoàn thành check-in/check-out ở đi địa điểm hiện tại";
        }

        return AttendanceStatusModel.builder()
                .workDate(workDate)
                .checkInTime(checkInTime)
                .checkOutTime(checkOutTime)
                .isCheckInValid(isCheckInValid)
                .isCheckOutValid(isCheckOutValid)
                .checkInMessage(generateCheckInMessage(attendance.getCheckInTime()))
                .checkOutMessage(generateCheckOutMessage(attendance.getCheckOutTime()))
                .canCheckIn(canCheckIn)
                .canCheckOut(isSessionOpen)
                .sessionOpen(isSessionOpen)
                .openSessionBranchId(openSessionBranchId)
                .currentBranchId(currentBranchId)
                .canResetByBranchChange(canResetByBranchChange)
                .statusMessage(statusMessage)
                .build();
    }

    /**
     * Process check-in
     */
    @Override
    public AttendanceLogModel checkIn(CheckInCommand command) {

        log.info("Processing check-in for userId: {}", command.getUserId());

        // 1. Validate user exists
        UserModel user =
                userRepository
                        .findById(command.getUserId())
                        .orElseThrow(() -> new NotFoundException("User không tồn tại"));

        // 2. Validate location: WiFi (IP) first, then GPS fallback
        UUID checkInBranchId =
                validateCompanyAccess(
                        command.getClientIp(),
                        command.getLatitude(),
                        command.getLongitude(),
                        "check-in");

        long checkInTimestamp = command.getCheckInTime();
        if (checkInTimestamp <= 0) {
            checkInTimestamp = nowMillis();
        }

        LocalDate workDate = convertToLocalDate(checkInTimestamp);

        // 3. Load existing record with pessimistic write lock to prevent duplicate inserts
        //    under concurrent requests for the same user+date.
        Optional<AttendanceLogModel> attendanceTodayOpt =
                attendanceRepository.findByUserAndDateForUpdate(command.getUserId(), workDate);

        if (attendanceTodayOpt.isPresent()) {
            AttendanceLogModel attendance = attendanceTodayOpt.get();

            // If scheduler already marked ABSENT, update that record instead of creating a new one.
            if (AttendanceStatus.ABSENT.equals(attendance.getAttendanceStatus())) {
                boolean isValid = validateCheckInTime(checkInTimestamp);

                attendance.setCheckInTime(checkInTimestamp);
                attendance.setCheckOutTime(0);
                attendance.setCheckInBranchId(checkInBranchId);
                attendance.setCheckInValid(isValid);
                attendance.setAttendanceStatus(AttendanceStatus.CHECK_IN_LATE);

                attendance = attendanceRepository.update(attendance);
                log.info("User {} checked in after being marked ABSENT (isValid: {})",
                        command.getUserId(), isValid);
                return attendance;
            }

            // Any other status means user already checked in today — reject.
            throw new BadRequestException(
                    AttendanceError.ALREADY_CHECKED_IN.getValue(),
                    "Bạn đã check-in trong hôm nay");
        }

        // 4. Kiểm tra xem đã check-in ở branch này chưa (guard for non-ABSENT duplicates)
        boolean alreadyCheckedInThisBranch =
                attendanceRepository.existsCheckedInBranchByUserAndDate(
                        command.getUserId(), workDate, checkInBranchId);

        if (alreadyCheckedInThisBranch) {
            throw new BadRequestException(
                    AttendanceError.ALREADY_CHECKED_IN.getValue(),
                    "Bạn đã check-in ở văn phòng này trong hôm nay");
        }

        // 5. Kiểm tra xem có session check-in chưa checkout không
        Optional<AttendanceLogModel> openAttendanceOpt =
                attendanceRepository.findOpenSessionByUserAndDate(
                        command.getUserId(), workDate);

        if (openAttendanceOpt.isPresent()) {
            throw new BadRequestException(
                    AttendanceError.OPEN_SESSION_EXISTS.getValue(),
                    "Bạn phải checkout địa điểm Onsite trước đó");
        }

        // 6. Validate check-in time (Standard 8:45)
        boolean isValid = validateCheckInTime(checkInTimestamp);

        // 7. Tạo attendance mới
        AttendanceLogModel attendance =
                AttendanceLogModel.builder()
                        .attendanceId(snowflake.next())
                        .user(user)
                        .workDate(workDate)
                        .checkInTime(checkInTimestamp)
                        .checkOutTime(0)
                        .isCheckInValid(isValid)
                        .isCheckOutValid(false)
                        .source("WEB")
                        .checkInBranchId(checkInBranchId)
                        .attendanceStatus(isValid ? AttendanceStatus.CHECK_IN_ON_TIME : AttendanceStatus.CHECK_IN_LATE)
                        .build();

        attendance = attendanceRepository.save(attendance);

        log.info("Check-in successful for userId: {}, isValid: {}", command.getUserId(), isValid);
        return attendance;
    }


    /**
     * Process check-out
     */
    @Override
    public AttendanceLogModel checkOut(CheckOutCommand command) {
        log.info("Processing check-out for userId: {}", command.getUserId());

        // Validate location: WiFi (IP) first, then GPS fallback
        UUID checkOutBranchId =
                validateCompanyAccess(
                        command.getClientIp(), command.getLatitude(), command.getLongitude(), "check-out");

        long checkOutTime = command.getCheckOutTime();
        if (checkOutTime <= 0) {
            checkOutTime = nowMillis();
        }

        LocalDate workDate = convertToLocalDate(checkOutTime);
        AttendanceLogModel attendance =
                attendanceRepository
                        .findOpenSessionByUserAndDate(command.getUserId(), workDate)
                        .orElseThrow(
                                () ->
                                        new BadRequestException(
                                                AttendanceError.OPEN_SESSION_NOT_FOUND.getValue(),
                                                "Bạn không có phiên check-in mở trong hôm nay"));

        // Validate check-out time
        boolean isValid = validateCheckOutTime(checkOutTime);

        attendance.setCheckOutTime(checkOutTime);
        attendance.setCheckOutValid(isValid);
        attendance.setCheckOutBranchId(checkOutBranchId);

        if (isValid) {
            attendance.setAttendanceStatus(AttendanceStatus.CHECK_OUT_ON_TIME);
        } else {
            attendance.setAttendanceStatus(AttendanceStatus.CHECK_OUT_EARLY);
        }

        attendance = attendanceRepository.update(attendance);

        log.info("Check-out successful for userId: {}, isValid: {}", command.getUserId(), isValid);
        return attendance;
    }

    @Override
    public int autoCheckoutOpenAttendances(long checkOutTimeMillis) {
        long effectiveCheckOutTime = checkOutTimeMillis > 0 ? checkOutTimeMillis : nowMillis();
        LocalDate workDate = convertToLocalDate(effectiveCheckOutTime);
        List<AttendanceLogModel> openAttendances = attendanceRepository.findAllOpenByDate(workDate);

        int processed = 0;
        for (AttendanceLogModel attendance : openAttendances) {
            if (attendance.getCheckInTime() <= 0 || attendance.getCheckOutTime() > 0) {
                continue;
            }

            boolean isValid = validateCheckOutTime(effectiveCheckOutTime);
            attendance.setCheckOutTime(effectiveCheckOutTime);
            attendance.setCheckOutValid(isValid);
            attendance.setCheckOutBranchId(
                    attendance.getCheckOutBranchId() != null
                            ? attendance.getCheckOutBranchId()
                            : attendance.getCheckInBranchId());
            attendance.setAttendanceStatus(
                    isValid ? AttendanceStatus.CHECK_OUT_ON_TIME : AttendanceStatus.CHECK_OUT_EARLY);
            attendanceRepository.update(attendance);
            processed++;
        }

        log.info(
                "Auto check-out completed for date {} at {} - processed {} open attendances",
                workDate,
                effectiveCheckOutTime,
                processed);
        return processed;
    }

    @Override
    public List<AttendanceInWeekCommand> getAttendanceInWeekByUserId(Long userId) {
        return attendanceRepository.getAttendanceInWeekByUserId(userId);
    }

    // ==================== Helper Methods ====================

    private UUID validateCompanyAccess(
            String clientIp, Double latitude, Double longitude, String action) {
        UUID branchIdFromIp = networkCheckPort.resolveCompanyIpBranchId(clientIp).orElse(null);
        UUID branchIdFromLocation =
                networkCheckPort.resolveCompanyLocationBranchId(latitude, longitude).orElse(null);
        boolean isCorrectIp = branchIdFromIp != null;
        boolean isCorrectLocation = branchIdFromLocation != null;

        if (!isCorrectIp && !isCorrectLocation) {
            log.warn(
                    "{} rejected: IP {} and location ({}, {}) are both invalid",
                    action,
                    clientIp,
                    latitude,
                    longitude);
            throw new BadRequestException(
                    AttendanceError.OUT_OF_RANGE.getValue(),
                    "Hệ thống đang ghi nhận vị trí của bạn sai (hoặc ngoài bán kính cho phép) hoặc chưa có phiếu làm Remote. Vui lòng tạo phiếu ");
        }

        return branchIdFromIp != null ? branchIdFromIp : branchIdFromLocation;
    }

    private UUID resolveCurrentBranchId(String clientIp, Double latitude, Double longitude) {
        UUID branchIdFromIp = networkCheckPort.resolveCompanyIpBranchId(clientIp).orElse(null);
        if (branchIdFromIp != null) {
            return branchIdFromIp;
        }
        return networkCheckPort.resolveCompanyLocationBranchId(latitude, longitude).orElse(null);
    }

    private boolean validateCheckInTime(long checkInTimeMillis) {
        LocalTime checkIn = convertToLocalTime(checkInTimeMillis);
        LocalTime standardTime = LocalTime.of(STANDARD_CHECK_IN_HOUR, STANDARD_CHECK_IN_MINUTE);
        return !checkIn.isAfter(standardTime);
    }

    private boolean validateCheckOutTime(long checkOutTimeMillis) {
        LocalTime checkOut = convertToLocalTime(checkOutTimeMillis);
        LocalTime standardTime = LocalTime.of(STANDARD_CHECK_OUT_HOUR, STANDARD_CHECK_OUT_MINUTE);
        return !checkOut.isBefore(standardTime);
    }

    public static LocalTime convertToLocalTime(long millis) {
        return Instant.ofEpochMilli(millis).atZone(CoreConstant.VIETNAM_ZONE).toLocalTime();
    }

    private LocalDate convertToLocalDate(long millis) {
        return Instant.ofEpochMilli(millis).atZone(CoreConstant.VIETNAM_ZONE).toLocalDate();
    }

    private long nowMillis() {
        return ZonedDateTime.now(ZoneId.of(timezoneConfig)).toInstant().toEpochMilli();
    }

    // --- Static Helper Methods for Message Generation (used by Mapper) ---

    public static String generateCheckInMessage(long checkInTimeMillis) {
        if (checkInTimeMillis <= 0) {
            return null;
        }
        LocalTime checkInTime = convertToLocalTime(checkInTimeMillis);
        String timeStr = checkInTime.format(TIME_FORMATTER);
        LocalTime standardTime = LocalTime.of(STANDARD_CHECK_IN_HOUR, STANDARD_CHECK_IN_MINUTE);
        boolean isValid = !checkInTime.isAfter(standardTime);

        if (isValid) {
            return String.format("Check in thành công (%s) đúng giờ", timeStr);
        } else {
            return String.format("Check in thành công (%s) trễ hơn 8:45", timeStr);
        }
    }

    public static String generateCheckOutMessage(long checkOutTimeMillis) {
        if (checkOutTimeMillis <= 0) {
            return null;
        }
        LocalTime checkOutTime = convertToLocalTime(checkOutTimeMillis);
        String timeStr = checkOutTime.format(TIME_FORMATTER);
        LocalTime standardTime = LocalTime.of(STANDARD_CHECK_OUT_HOUR, STANDARD_CHECK_OUT_MINUTE);
        boolean isValid = !checkOutTime.isBefore(standardTime);

        if (isValid) {
            return String.format("Check out thành công (%s) đúng giò", timeStr);
        } else {
            return String.format("Check out thành công (%s) sớm hơn 17:15", timeStr);
        }
    }

    @Override
    public PaginatedData<AttendanceLogModel> filterAttendance(
            String nameOrEmail,
            LocalDate startDate,
            LocalDate endDate,
            String attendanceStatus,
            int page,
            int size) {

        AttendanceStatus statusEnum = null;

        if (attendanceStatus != null && !attendanceStatus.isBlank()) {
            statusEnum = AttendanceStatus.valueOf(attendanceStatus);
        }

        FilterAttendanceCommand command = FilterAttendanceCommand.builder()
                .nameOrEmail(nameOrEmail)
                .startDate(startDate)
                .endDate(endDate)
                .attendanceStatus(statusEnum)
                .build();

        return attendanceRepository.filterAttendanceLogs(
                command,
                page,
                size
        );
    }
}
