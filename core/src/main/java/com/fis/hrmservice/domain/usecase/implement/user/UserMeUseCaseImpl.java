package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.resonse.InternalUserCoreResponse;
import com.fis.hrmservice.domain.port.output.feign.CreateAuthIdentityPort;
import com.fis.hrmservice.domain.port.output.feign.SidebarMenuPort;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMeUseCaseImpl {

  UserProfileUseCaseImpl userProfileUseCase;
  CreateAuthIdentityPort createAuthIdentityPort;
  SidebarMenuPort sidebarMenuPort;

  public InternalUserCoreResponse getMe(Long userId) {
    var user = userProfileUseCase.internalUserProfile(userId);
    var userRole = createAuthIdentityPort.getRoleByUserId(userId);

    return InternalUserCoreResponse.builder()
        .fullName(user.getFullName())
        .avatarUrl(user.getAvatarUrl())
        .email(user.getCompanyEmail())
        .role(userRole == null ? null : userRole.getName())
        .roleId(parseRoleId(userRole == null ? null : userRole.getId()))
        .positionName(user.getPosition() == null ? null : user.getPosition().getName())
        .isFaceRegistry(user.getIsFaceRegistry())
        .sidebarMenus(resolveSidebarMenus(userRole == null ? null : userRole.getName()))
        .build();
  }

  private List<com.fis.hrmservice.domain.model.resonse.SidebarMenuCoreResponse> resolveSidebarMenus(
      String roleName) {
    if (roleName == null || roleName.isBlank()) {
      return List.of();
    }
    return sidebarMenuPort.getSidebarMenusByRoles(List.of(roleName));
  }

  private Long parseRoleId(String roleId) {
    if (roleId == null || roleId.isBlank()) {
      return null;
    }
    try {
      return Long.parseLong(roleId);
    } catch (NumberFormatException ex) {
      return null;
    }
  }
}
