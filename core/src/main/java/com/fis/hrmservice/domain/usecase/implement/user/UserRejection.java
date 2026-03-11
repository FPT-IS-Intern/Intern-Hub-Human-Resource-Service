package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRejection {

  UserRepositoryPort userRepositoryPort;

  @Transactional
  public UserModel rejectUser(Long userId) {

    UserModel user =
        userRepositoryPort
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));

    // ✅ Guard trạng thái
    if (user.getSysStatus() != UserStatus.PENDING) {
      throw new ConflictDataException("Only PENDING users can be rejected");
    }

    int updated = userRepositoryPort.updateStatus(userId, UserStatus.REJECTED);

    if (updated != 1) {
      throw new ConflictDataException("Cannot reject user");
    }

    user.setSysStatus(UserStatus.REJECTED); // tránh stale data
    return user;
  }
}
