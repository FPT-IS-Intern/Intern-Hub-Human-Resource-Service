package com.fis.hrmservice.domain.port.output;

public interface AvatarRepositoryPort {
    String getAvatarUrlByUserId(Long userId);
}