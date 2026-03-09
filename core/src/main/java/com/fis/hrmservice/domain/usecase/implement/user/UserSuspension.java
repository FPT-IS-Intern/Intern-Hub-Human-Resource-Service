package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.feign.CreateAuthIdentityPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSuspension {

  private final UserRepositoryPort userRepositoryPort;

  private final CreateAuthIdentityPort createAuthIdentityPort;

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

    TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
              @Override
              public void afterCommit() {
                try {
                  createAuthIdentityPort.createAuthIdentity(
                          model.getUserId(),
                          model.getCompanyEmail()
                  );
                } catch (Exception e) {
                  log.error("Failed to create auth identity after commit for userId: {}",
                          model.getUserId(), e);
                }
              }
            }
    );

    return userRepositoryPort
        .findById(userId)
        .orElseThrow(() -> new NotFoundException("User not found after update"));
  }
}
