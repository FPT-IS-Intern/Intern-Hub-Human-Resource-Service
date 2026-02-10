package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.CreateAuthIdentityPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserApproval {

  private final UserRepositoryPort userRepositoryPort;

  private final CreateAuthIdentityPort createAuthIdentityPort;

  @Transactional
  public UserModel approveUser(Long userId) {

    Long updated = userRepositoryPort.updateStatus(userId, UserStatus.APPROVED);

    if (updated != 1) {
      throw new ConflictDataException("Cannot approve user");
    }

    UserModel userModel =
        userRepositoryPort
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));

    try {
      createAuthIdentityPort.createAuthIdentity(userModel.getUserId(), userModel.getCompanyEmail());
    } catch (Exception e) {
      log.error("Failed to create auth identity for userId: {}, error: {}", userId, e.getMessage());
      throw new RuntimeException("Failed to create auth identity");
    }

    return userModel;
  }

  private void createUserAccount(UserModel user) {}
}
