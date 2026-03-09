package com.fis.hrmservice.infra.persistence.adapter.user;

import com.fis.hrmservice.domain.model.user.AvatarModel;
import com.fis.hrmservice.domain.port.output.user.AvatarRepositoryPort;
import com.fis.hrmservice.infra.mapper.AvatarMapper;
import com.fis.hrmservice.infra.persistence.entity.Avatar;
import com.fis.hrmservice.infra.persistence.entity.User;
import com.fis.hrmservice.infra.persistence.repository.user.AvatarRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AvatarRepositoryAdapter implements AvatarRepositoryPort {

  private final AvatarRepository avatarRepository;

  private final AvatarMapper avatarMapper;

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public AvatarModel getAvatarByUserId(Long userId) {
    return avatarMapper.toModel(avatarRepository.findAvatarByUserId(userId));
  }

  @Override
  public AvatarModel save(AvatarModel avatar) {

    Avatar entity = avatarMapper.toEntity(avatar);

    // load user từ DB để Hibernate quản lý
    User managedUser = entityManager.getReference(User.class, avatar.getUser().getUserId());
    entity.setUser(managedUser);

    return avatarMapper.toModel(avatarRepository.save(entity));
  }

  @Override
  public Map<Long, AvatarModel> getAvatarsByUserIds(List<Long> userIds) {

    if (userIds == null || userIds.isEmpty()) {
      return Collections.emptyMap();
    }

    List<Avatar> avatars = avatarRepository.findByUserUserIdIn(userIds);

    return avatars.stream()
        .collect(Collectors.toMap(avatar -> avatar.getUser().getId(), avatarMapper::toModel));
  }
}
