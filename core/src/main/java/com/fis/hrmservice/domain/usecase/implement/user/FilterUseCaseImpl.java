package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.input.user.FilterUserUseCase;
import com.fis.hrmservice.domain.port.output.user.AvatarRepositoryPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.user.FilterUserCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FilterUseCaseImpl implements FilterUserUseCase {

  private final UserRepositoryPort userRepositoryPort;
  private final AvatarRepositoryPort avatarRepositoryPort;

  @Override
  public List<UserModel> filterUsers(FilterUserCommand command) {
    List<UserModel> users = userRepositoryPort.filterUser(command);

    users.forEach(
        user -> {
          String avatarUrl = avatarRepositoryPort.getAvatarUrlByUserId(user.getUserId());
          user.setAvatarUrl(avatarUrl);
        });

    return users;
  }
}
