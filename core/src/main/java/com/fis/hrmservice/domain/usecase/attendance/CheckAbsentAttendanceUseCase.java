package com.fis.hrmservice.domain.usecase.attendance;

import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.model.constant.AttendanceStatus;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.attendance.AttendanceRepositoryPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.intern.hub.library.common.utils.Snowflake;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckAbsentAttendanceUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final AttendanceRepositoryPort attendanceLogRepositoryPort;
    private final Snowflake snowflake;

    private static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    @Transactional
    public void execute() {

        LocalDate today = LocalDate.now(VIETNAM_ZONE);

        List<UserModel> users = userRepositoryPort.findAllActiveUsers();

        int absentCount = 0;
        int skippedCount = 0;
        int errorCount = 0;

        for (UserModel user : users) {
            try {
                boolean checked = attendanceLogRepositoryPort
                        .existsByUserIdAndWorkDate(user.getUserId(), today);

                if (!checked) {
                    AttendanceLogModel attendanceLog = AttendanceLogModel.builder()
                            .attendanceId(snowflake.next())
                            .user(user)
                            .workDate(today)
                            .attendanceStatus(AttendanceStatus.ABSENT)
                            .build();

                    attendanceLogRepositoryPort.save(attendanceLog);
                    absentCount++;
                } else {
                    skippedCount++;
                }
            } catch (Exception e) {
                errorCount++;
                log.error("Failed to mark absent for userId: {}", user.getUserId(), e);
            }
        }

        log.info("Absent attendance check completed for date {}: total={}, marked_absent={}, already_checked_in={}, errors={}",
                today, users.size(), absentCount, skippedCount, errorCount);
    }
}