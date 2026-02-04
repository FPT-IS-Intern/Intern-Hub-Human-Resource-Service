package com.fis.hrmservice.domain.port.output.user;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.command.user.FilterUserCommand;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
  UserModel save(UserModel user);

  Optional<UserModel> findById(Long userId);

  Optional<UserModel> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByIdNumber(String idNumber);

  List<UserModel> findAll();

  List<UserModel> filterUser(FilterUserCommand command);
}
