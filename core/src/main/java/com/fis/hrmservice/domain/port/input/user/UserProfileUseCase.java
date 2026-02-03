package com.fis.hrmservice.domain.port.input.user;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.command.UpdateUserProfileCommand;

public interface UserProfileUseCase {
    UserModel getUserProfile(Long userId);
    UserModel updateProfileUser(UpdateUserProfileCommand command, long userId);
}
