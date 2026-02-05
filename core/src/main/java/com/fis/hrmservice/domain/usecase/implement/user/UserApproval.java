package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.input.user.ApprovalUser;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.intern.hub.library.common.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserApproval {

  @Autowired
  private UserRepositoryPort userRepositoryPort;

  public UserModel approveUser(Long userId) {
    UserModel userModel =
        userRepositoryPort
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

    userModel.setSysStatus(UserStatus.APPROVED);
    userRepositoryPort.save(userModel);

    return userModel;
  }
}
