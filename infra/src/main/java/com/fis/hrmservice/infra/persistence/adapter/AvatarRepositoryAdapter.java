package com.fis.hrmservice.infra.persistence.adapter;

import com.fis.hrmservice.domain.port.output.AvatarRepositoryPort;
import com.fis.hrmservice.infra.persistence.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AvatarRepositoryAdapter implements AvatarRepositoryPort {

    @Autowired
    private AvatarRepository avatarRepository;

    @Override
    public String getAvatarUrlByUserId(Long userId) {
        return avatarRepository.findAvatarUrlByUserId(userId);
    }
}
