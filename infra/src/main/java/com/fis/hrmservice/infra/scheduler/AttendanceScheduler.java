package com.fis.hrmservice.infra.scheduler;

import com.fis.hrmservice.domain.usecase.attendance.CheckAbsentAttendanceUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AttendanceScheduler {

    private final CheckAbsentAttendanceUseCase checkAbsentAttendanceUseCase;

    // 8:45 AM mỗi ngày
    @Scheduled(cron = "0 45 8 * * *")
    public void checkAbsentAttendance() {
        log.info("Start checking absent attendance");
        checkAbsentAttendanceUseCase.execute();
    }
}