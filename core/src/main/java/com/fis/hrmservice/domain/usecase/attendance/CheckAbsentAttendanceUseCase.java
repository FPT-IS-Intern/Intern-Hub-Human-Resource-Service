package com.fis.hrmservice.domain.usecase.attendance;

import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.model.constant.AttendanceStatus;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.attendance.AttendanceRepositoryPort;
import com.fis.hrmservice.domain.port.output.ticket.TicketRepositoryPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.intern.hub.library.common.utils.Snowflake;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckAbsentAttendanceUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final AttendanceRepositoryPort attendanceLogRepositoryPort;
    private final TicketRepositoryPort ticketRepositoryPort;
    private final Snowflake snowflake;

    private static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    @Transactional
    public void execute() {

        LocalDate today = LocalDate.now(VIETNAM_ZONE);
        LocalDate yesterday = today.minusDays(1);

        // Bỏ qua nếu ngày hôm qua là Thứ 7 hoặc Chủ Nhật
        if (yesterday.getDayOfWeek() == DayOfWeek.SATURDAY || yesterday.getDayOfWeek() == DayOfWeek.SUNDAY) {
            log.info("Skipping absent attendance check for {} (Weekend)", yesterday);
            return;
        }

        List<UserModel> users = userRepositoryPort.findAllActiveUsers();

        int absentCount = 0;
        int skippedCount = 0;
        int onLeaveCount = 0;
        int errorCount = 0;

        for (UserModel user : users) {
            try {
                // 1. Kiểm tra xem đã có log attendance chưa
                boolean checked = attendanceLogRepositoryPort
                        .existsByUserIdAndWorkDate(user.getUserId(), yesterday);

                if (checked) {
                    skippedCount++;
                    continue;
                }

                // 2. Kiểm tra xem có đơn xin nghỉ (Leave/Remote/...) đã được duyệt không
                boolean hasApprovedTicket = ticketRepositoryPort
                        .existsApprovedTicketByUserIdAndDate(user.getUserId(), yesterday);

                if (hasApprovedTicket) {
                    onLeaveCount++;
                    continue;
                }

                // 3. Đánh dấu ABSENT
                AttendanceLogModel attendanceLog = AttendanceLogModel.builder()
                        .attendanceId(snowflake.next())
                        .user(user)
                        .workDate(yesterday)
                        .attendanceStatus(AttendanceStatus.ABSENT)
                        .build();

                attendanceLogRepositoryPort.save(attendanceLog);
                absentCount++;

            } catch (Exception e) {
                errorCount++;
                log.error("Failed to mark absent for userId: {}", user.getUserId(), e);
            }
        }

        log.info("Absent attendance check completed for date {}: total={}, marked_absent={}, on_leave={}, already_checked_in={}, errors={}",
                yesterday, users.size(), absentCount, onLeaveCount, skippedCount, errorCount);
    }
}