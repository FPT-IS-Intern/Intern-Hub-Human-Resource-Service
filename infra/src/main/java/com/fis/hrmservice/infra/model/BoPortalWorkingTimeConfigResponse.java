package com.fis.hrmservice.infra.model;

import java.time.LocalTime;

public record BoPortalWorkingTimeConfigResponse(
    LocalTime workStartTime, LocalTime workEndTime, LocalTime autoCheckoutTime) {}
