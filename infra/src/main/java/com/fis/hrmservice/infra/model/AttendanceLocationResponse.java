package com.fis.hrmservice.infra.model;

public record AttendanceLocationResponse(
        String name, Double latitude, Double longitude, Integer radiusMeters) {
}
