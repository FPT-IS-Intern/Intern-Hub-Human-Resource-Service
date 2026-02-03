package com.fis.hrmservice.domain.usecase.implement;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.input.user.FilterUserUseCase;
import com.fis.hrmservice.domain.port.output.AvatarRepositoryPort;
import com.fis.hrmservice.domain.port.output.UserRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.FilterUserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class FilterUseCaseImpl implements FilterUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final AvatarRepositoryPort avatarRepositoryPort;

    @Override
    public List<UserModel> filterUsers(FilterUserCommand command) {
        List<UserModel> users = userRepositoryPort.filterUser(command);

        users.forEach(user -> {
            String avatarUrl =
                    avatarRepositoryPort.getAvatarUrlByUserId(user.getUserId());
            user.setAvatarUrl(avatarUrl);
        });

        return users;
    }
}