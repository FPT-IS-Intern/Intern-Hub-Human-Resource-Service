package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.dto.resonse.AuthIdentityStatusCoreResponse;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.feign.CreateAuthIdentityPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.user.FilterUserCommand;
import com.intern.hub.library.common.dto.PaginatedData;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilterUseCaseImpl {

  UserRepositoryPort userRepositoryPort;

  CreateAuthIdentityPort createAuthIdentityPort;

  public PaginatedData<UserModel> filterUsers(FilterUserCommand command, int page, int size) {

    PaginatedData<UserModel> result = userRepositoryPort.filterUser(command, page, size);

    List<UserModel> users = result.getItems().stream().toList();

    if (users == null || users.isEmpty()) {
      return result;
    }

    List<Long> userIds = users.stream()
        .map(UserModel::getUserId)
        .toList();

    Map<Long, String> statusMap = createAuthIdentityPort.getIdentityStatuses(userIds).stream()
        .collect(Collectors.toMap(
            AuthIdentityStatusCoreResponse::userId,
            AuthIdentityStatusCoreResponse::sysStatus,
            (existing, replacement) -> existing
        ));

    for (UserModel user : users) {
      user.setAuthIdentityStatus(statusMap.get(user.getUserId()));
    }

    return result;
  }
}
