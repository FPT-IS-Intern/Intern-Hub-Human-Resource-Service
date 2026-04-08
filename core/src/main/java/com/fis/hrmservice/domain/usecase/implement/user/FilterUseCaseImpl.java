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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilterUseCaseImpl {

  UserRepositoryPort userRepositoryPort;

  CreateAuthIdentityPort createAuthIdentityPort;

  public PaginatedData<UserModel> filterUsers(FilterUserCommand command, int page, int size) {
    FilterUserCommand effectiveCommand = enrichRoleFilterFromAuth(command);
    if (effectiveCommand == null) {
      return PaginatedData.<UserModel>builder()
          .items(List.of())
          .totalItems(0)
          .totalPages(0)
          .build();
    }

    PaginatedData<UserModel> result = userRepositoryPort.filterUser(effectiveCommand, page, size);

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

  private FilterUserCommand enrichRoleFilterFromAuth(FilterUserCommand command) {
    if (command.getRoles() == null || command.getRoles().isEmpty()) {
      return command;
    }

    Set<String> requestedRoles = command.getRoles().stream()
        .filter(role -> role != null && !role.isBlank())
        .map(String::trim)
        .collect(Collectors.toSet());

    Set<String> normalizedRequestedRoles = requestedRoles.stream()
        .map(this::normalizeRoleToken)
        .collect(Collectors.toSet());

    if (requestedRoles.isEmpty()) {
      return command;
    }

    List<String> matchedRoleIds = createAuthIdentityPort.getAllRoles().stream()
        .filter(role -> role != null && role.getName() != null && role.getId() != null)
        .filter(role -> requestedRoles.contains(role.getId().trim())
            || normalizedRequestedRoles.contains(normalizeRoleToken(role.getName())))
        .map(role -> role.getId().trim())
        .filter(roleId -> !roleId.isBlank())
        .toList();

    if (matchedRoleIds.isEmpty()) {
      return null;
    }

    Set<Long> matchedUserIds = new HashSet<>();
    for (String roleId : matchedRoleIds) {
      matchedUserIds.addAll(createAuthIdentityPort.getUsersByRoleId(roleId));
    }

    if (matchedUserIds.isEmpty()) {
      return null;
    }

    return FilterUserCommand.builder()
        .keyword(command.getKeyword())
        .sysStatuses(command.getSysStatuses())
        .roles(command.getRoles())
        .positions(command.getPositions())
        .roleUserIds(new ArrayList<>(matchedUserIds))
        .build();
  }

  private String normalizeRoleToken(String value) {
    if (value == null) {
      return "";
    }
    String normalized = value.trim().toUpperCase()
        .replace(' ', '_')
        .replace('-', '_');
    if (normalized.startsWith("ROLE_")) {
      normalized = normalized.substring(5);
    }
    return normalized;
  }
}
