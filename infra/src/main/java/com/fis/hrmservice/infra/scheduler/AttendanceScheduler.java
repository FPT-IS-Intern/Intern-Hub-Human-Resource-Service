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

    // 1:00 AM mỗi ngày (Vietnam timezone) để check ABSENT cho ngày hôm trước
    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Ho_Chi_Minh")
    public void checkAbsentAttendance() {
        log.info("Start checking absent attendance");
        long start = System.currentTimeMillis();
        try {
            checkAbsentAttendanceUseCase.execute();
            log.info("Finished checking absent attendance in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Failed to check absent attendance after {}ms", System.currentTimeMillis() - start, e);
        }
    }
}