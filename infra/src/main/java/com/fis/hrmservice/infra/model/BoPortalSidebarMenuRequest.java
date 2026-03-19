package com.fis.hrmservice.infra.model;

import java.util.List;

public record BoPortalSidebarMenuRequest(
    String role,
    List<String> roles
) {}
