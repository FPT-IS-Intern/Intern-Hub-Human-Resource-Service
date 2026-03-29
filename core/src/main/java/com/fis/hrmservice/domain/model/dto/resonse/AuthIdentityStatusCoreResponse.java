package com.fis.hrmservice.domain.model.dto.resonse;

public record AuthIdentityStatusCoreResponse(
        Long userId,
        String sysStatus
) {
}
