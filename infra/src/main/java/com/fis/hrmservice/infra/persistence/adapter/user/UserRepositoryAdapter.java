package com.fis.hrmservice.infra.persistence.adapter.user;

import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.user.FilterUserCommand;
import com.fis.hrmservice.infra.mapper.UserMapper;
import com.fis.hrmservice.infra.persistence.entity.User;
import com.fis.hrmservice.infra.persistence.repository.user.UserJpaRepository;
import com.intern.hub.library.common.dto.PaginatedData;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

  private final UserJpaRepository userJpaRepository;

  private final UserMapper userMapper;

  @Override
  @Transactional
  public UserModel save(UserModel user) {
    User entity = userMapper.toEntity(user);

    // Fix bidirectional relationship: avatarToEntity/cvToEntity ignore the user field,
    // so we must set it manually to prevent cascade from nullifying the FK.
    if (entity.getAvatar() != null) {
      entity.getAvatar().setUser(entity);
    }
    if (entity.getCv() != null) {
      entity.getCv().setUser(entity);
    }

    User savedEntity = userJpaRepository.save(entity);
    return userMapper.toModel(savedEntity);
  }

  @Override
  public Optional<UserModel> findById(Long userId) {
    return Optional.ofNullable(userMapper.toModel(userJpaRepository.findById(userId).get()));
  }

  @Override
  public Optional<UserModel> findByEmail(String email) {
    return Optional.ofNullable(
        userMapper.toModel(userJpaRepository.findByCompanyEmail(email).get()));
  }

  @Override
  public boolean existsByEmail(String email) {
    return userJpaRepository.existsByCompanyEmail(email);
  }

  @Override
  public boolean existsByIdNumber(String idNumber) {
    return userJpaRepository.existsByIdNumber(idNumber);
  }

  @Override
  public List<UserModel> findAll() {
    return List.of();
  }

  @Override
  public PaginatedData<UserModel> filterUser(FilterUserCommand command, int page, int size) {

    Page<User> userPage = userJpaRepository.filterUser(command, PageRequest.of(page, size));

    List<UserModel> models = userMapper.toResponseList(userPage.getContent());

    return PaginatedData.<UserModel>builder()
        .items(models)
        .totalItems(userPage.getTotalElements())
        .totalPages(userPage.getTotalPages())
        .build();
  }

  @Override
  public Long updateStatus(Long userId, UserStatus status) {
    return userJpaRepository.updateStatus(userId, status);
  }

  @Override
  public UserModel internalUserProfile(Long userId) {
    return userMapper.toModel(userJpaRepository.internalUserProfile(userId));
  }

  @Override
  public Long suspendUser(Long userId, UserStatus status) {
    return userJpaRepository.suspendUser(userId, status);
  }

  @Override
  public int totalIntern() {
    return userJpaRepository.totalIntern();
  }

  @Override
  public int internshipChanging() {
    return userJpaRepository.internshipChanging();
  }

  @Override
  public List<UserModel> listAllSupervisor() {
    return userJpaRepository.listAllSupervisor().stream().map(userMapper::toModel).toList();
  }
}
