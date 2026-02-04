package com.fis.hrmservice.infra.persistence.adapter.user;

import com.fis.hrmservice.domain.port.output.user.AvatarRepositoryPort;
import com.fis.hrmservice.infra.persistence.repository.user.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AvatarRepositoryAdapter implements AvatarRepositoryPort {

  @Autowired private AvatarRepository avatarRepository;

  @Override
  public String getAvatarUrlByUserId(Long userId) {
    return avatarRepository.findAvatarUrlByUserId(userId);
  }
}
