package com.fis.hrmservice.domain.model.attendance;

import java.time.LocalTime;

public record WorkingTimeConfigModel(
    LocalTime workStartTime, LocalTime workEndTime, LocalTime autoCheckoutTime) {}
