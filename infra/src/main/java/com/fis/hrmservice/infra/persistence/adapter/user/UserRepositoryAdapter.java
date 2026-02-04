package com.fis.hrmservice.infra.persistence.adapter.user;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.user.FilterUserCommand;
import com.fis.hrmservice.infra.mapper.UserMapper;
import com.fis.hrmservice.infra.persistence.entity.User;
import com.fis.hrmservice.infra.persistence.repository.user.UserJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryAdapter implements UserRepositoryPort {

  @Autowired private UserJpaRepository userJpaRepository;

  @Autowired private UserMapper userMapper;

  @Override
  public UserModel save(UserModel user) {
    User entity = userMapper.toEntity(user);
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
  public List<UserModel> filterUser(FilterUserCommand command) {
    return userMapper.toResponseList(userJpaRepository.filterUser(command));
  }
}
