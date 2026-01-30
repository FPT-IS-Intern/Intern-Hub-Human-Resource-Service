package com.fis.hrmservice.domain.usecase.implement;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.input.FilterUserUseCase;
import com.fis.hrmservice.domain.usecase.command.FilterUserCommand;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FilterUseCaseImpl implements FilterUserUseCase {

    private final FilterUserUseCase filterUserUseCase;

    @Override
    public List<UserModel> filterUserIdsByDepartmentIds(FilterUserCommand filterUserCommand) {
        return filterUserUseCase.filterUserIdsByDepartmentIds(filterUserCommand);
    }
}