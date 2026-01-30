package com.fis.hrmservice.domain.port.input;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.command.FilterUserCommand;

import java.util.List;

public interface FilterUserUseCase {
    List<UserModel> filterUserIdsByDepartmentIds(FilterUserCommand filterUserCommand);
}