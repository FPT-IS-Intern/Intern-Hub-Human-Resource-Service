package com.fis.hrmservice.domain.port.output.feign;

import com.fis.hrmservice.domain.model.resonse.AuthIdentityStatusCoreResponse;
import com.fis.hrmservice.domain.model.resonse.ListRoleCoreResponse;
import com.fis.hrmservice.domain.model.resonse.SetUserRoleCoreResponse;
import com.fis.hrmservice.domain.model.resonse.UserRoleCoreResponse;

import java.util.List;

public interface CreateAuthIdentityPort {

  void createAuthIdentity(Long userId, String email);

  void unlockAuthIdentity(Long userId);

  void lockAuthIdentity(Long userId);

  UserRoleCoreResponse getRoleByUserId(Long userId);

  void setUserRole(Long userId, Long roleId);

  List<ListRoleCoreResponse> getAllRoles();

  List<AuthIdentityStatusCoreResponse> getIdentityStatuses(List<Long> userIds);
}
