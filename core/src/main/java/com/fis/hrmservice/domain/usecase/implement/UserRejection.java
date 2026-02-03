package com.fis.hrmservice.domain.usecase.implement;

import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.input.user.RejectionUser;
import com.fis.hrmservice.domain.port.output.UserRepositoryPort;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserRejection implements RejectionUser {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UserModel rejectUser(Long userId) {

        UserModel userReject = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        if (userReject.getSysStatus().equals(UserStatus.REJECTED)) {
            throw new ConflictDataException("User " + userReject.getFullName() + " has been rejected");
        }

        userReject.setSysStatus(UserStatus.REJECTED);
        userRepositoryPort.save(userReject);

        return userReject;
    }
}
