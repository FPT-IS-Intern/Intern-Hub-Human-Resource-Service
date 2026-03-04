package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSuspension {

  private final UserRepositoryPort userRepositoryPort;

  @Transactional
  public UserModel suspendUser(Long userId) {

    UserModel model =
        userRepositoryPort
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));

    if (model.getSysStatus() == UserStatus.SUSPENDED) {
      throw new ConflictDataException("User is already suspended");
    }

    int suspended = userRepositoryPort.suspendUser(userId, UserStatus.SUSPENDED);

    if (suspended != 1) {
      throw new ConflictDataException("Cannot suspend user");
    }

    return userRepositoryPort
        .findById(userId)
        .orElseThrow(() -> new NotFoundException("User not found after update"));
  }
}
