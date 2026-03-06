package com.fis.hrmservice.domain.scheduler;

import com.fis.hrmservice.domain.usecase.attendance.AttendanceUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AttendanceAutoCheckoutScheduler {

  private final AttendanceUseCase attendanceUseCase;

  @Scheduled(cron = "0 30 17 * * *", zone = "Asia/Ho_Chi_Minh")
  public void autoCheckoutAt1730() {
    int processed = attendanceUseCase.autoCheckoutOpenAttendances(0L);
    log.info("Scheduled auto checkout at 17:30 completed - processed {}", processed);
  }
}
