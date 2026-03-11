package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.feign.CreateAuthIdentityPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserApproval {

  UserRepositoryPort userRepositoryPort;

  CreateAuthIdentityPort createAuthIdentityPort;

  @Transactional(rollbackFor = Exception.class)
  public UserModel approveUser(Long userId) {

    // 1️⃣ Load user
    UserModel user =
        userRepositoryPort
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));

    // 2️⃣ Validate trạng thái
    if (user.getSysStatus() != UserStatus.PENDING) {
      throw new ConflictDataException("User is not in PENDING state");
    }

    // 3️⃣ Tạo auth identity trước
    try {
      createAuthIdentityPort.createAuthIdentity(user.getUserId(), user.getCompanyEmail());
    } catch (Exception e) {
      log.error("Failed to create auth identity for userId: {}", userId, e);
      throw new RuntimeException("Failed to create auth identity", e);
    }

    // 4️⃣ Update status sau khi external call thành công
    int updated = userRepositoryPort.updateStatus(userId, UserStatus.APPROVED);

    if (updated != 1) {
      throw new ConflictDataException("Failed to update user status");
    }

    user.setSysStatus(UserStatus.APPROVED);
    return user;
  }

  public int totalIntern() {
    return userRepositoryPort.totalIntern();
  }

  public Integer internshipChanging() {
    return userRepositoryPort.internshipChanging();
  }
}
