package com.fis.hrmservice.infra.model;

import java.util.UUID;

public record AttendanceLocationResponse(
    String name, Double latitude, Double longitude, Integer radiusMeters, UUID branchId) {}
