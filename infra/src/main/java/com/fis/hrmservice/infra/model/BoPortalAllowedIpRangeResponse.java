package com.fis.hrmservice.infra.model;

public record BoPortalAllowedIpRangeResponse(
    long ipRangeId, String ipPrefix, String description, boolean isActive) {}
