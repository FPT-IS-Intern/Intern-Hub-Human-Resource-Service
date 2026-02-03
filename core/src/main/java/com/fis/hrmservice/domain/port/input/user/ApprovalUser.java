package com.fis.hrmservice.domain.port.input.user;

import com.fis.hrmservice.domain.model.user.UserModel;

public interface ApprovalUser {
    UserModel approveUser(Long userId);
}