package com.fis.hrmservice.domain.usecase.attendance;

import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.model.constant.AttendanceStatus;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.attendance.AttendanceRepositoryPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.intern.hub.library.common.utils.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckAbsentAttendanceUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final AttendanceRepositoryPort attendanceLogRepositoryPort;
    private final Snowflake snowflake;

    public void execute() {

        LocalDate today = LocalDate.now();

        List<UserModel> users = userRepositoryPort.findAllActiveUsers();

        for (UserModel user : users) {

            boolean checked = attendanceLogRepositoryPort
                    .existsByUserIdAndWorkDate(user.getUserId(), today);

            if (!checked) {

                AttendanceLogModel log = AttendanceLogModel.builder()
                        .attendanceId(snowflake.next())
                        .user(userRepositoryPort.findById(user.getUserId()).get())
                        .workDate(today)
                        .attendanceStatus(AttendanceStatus.ABSENT)
                        .build();

                attendanceLogRepositoryPort.save(log);
            }
        }
    }
}