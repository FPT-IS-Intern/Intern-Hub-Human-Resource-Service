package com.fis.hrmservice.infra.model;

import java.util.UUID;

public record BoPortalAllowedIpRangeResponse(
    String name, String ipPrefix, Boolean isActive, String description, UUID branchId) {}
