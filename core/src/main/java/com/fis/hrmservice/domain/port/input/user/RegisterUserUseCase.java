package com.fis.hrmservice.domain.port.input.user;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.command.user.RegisterUserCommand;

public interface RegisterUserUseCase {
  UserModel registerUser(RegisterUserCommand command);
}
