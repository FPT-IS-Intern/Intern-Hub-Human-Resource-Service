package com.fis.hrmservice.infra.model;

public record BoPortalAllowedIpRangeResponse(
    String name, String ipPrefix, Boolean isActive, String description) {}
