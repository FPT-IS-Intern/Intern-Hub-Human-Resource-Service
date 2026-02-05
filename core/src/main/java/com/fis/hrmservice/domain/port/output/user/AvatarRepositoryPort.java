package com.fis.hrmservice.domain.port.output.user;

import com.fis.hrmservice.domain.model.user.AvatarModel;

public interface AvatarRepositoryPort {
  AvatarModel getAvatarByUserId(Long userId);
}
