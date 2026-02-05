package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.user.AvatarModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.input.user.FilterUserUseCase;
import com.fis.hrmservice.domain.port.output.user.AvatarRepositoryPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.user.FilterUserCommand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilterUseCaseImpl {

    @Autowired
    private UserRepositoryPort userRepositoryPort;
    @Autowired
    private AvatarRepositoryPort avatarRepositoryPort;

    public List<UserModel> filterUsers(FilterUserCommand command) {
        List<UserModel> users = userRepositoryPort.filterUser(command);

        users.forEach(
                user -> {
                    AvatarModel avatarUrl = avatarRepositoryPort.getAvatarByUserId(user.getUserId());
                    user.setAvatar(avatarUrl);
                });

        return users;
    }
}
