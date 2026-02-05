package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRejection {

  @Autowired
  private UserRepositoryPort userRepositoryPort;

  public UserModel rejectUser(Long userId) {

    UserModel userReject =
        userRepositoryPort
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

    if (userReject.getSysStatus().equals(UserStatus.REJECTED)) {
      throw new ConflictDataException("User " + userReject.getFullName() + " has been rejected");
    }

    userReject.setSysStatus(UserStatus.REJECTED);
    userRepositoryPort.save(userReject);

    return userReject;
  }
}
