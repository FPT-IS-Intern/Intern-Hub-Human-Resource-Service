package com.fis.hrmservice.domain.model.resonse;

public record AuthIdentityStatusCoreResponse(
        Long userId,
        String sysStatus
) {
}
