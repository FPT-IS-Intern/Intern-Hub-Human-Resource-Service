package com.fis.hrmservice.infra.feign.response;

public record AuthIdentityStatusInfraResponse(
        Long userId,
        String sysStatus
) {
}
