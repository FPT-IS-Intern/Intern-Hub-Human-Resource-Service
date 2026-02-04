package com.fis.hrmservice.domain.port.output.user;

public interface AvatarRepositoryPort {
  String getAvatarUrlByUserId(Long userId);
}
