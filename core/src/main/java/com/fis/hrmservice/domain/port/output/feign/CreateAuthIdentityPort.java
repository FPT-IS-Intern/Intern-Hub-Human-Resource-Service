package com.fis.hrmservice.domain.port.output.feign;

import com.fis.hrmservice.domain.model.resonse.UserRoleCoreResponse;

public interface CreateAuthIdentityPort {

  void createAuthIdentity(Long userId, String email);

  void unlockAuthIdentity(Long userId);

  void lockAuthIdentity(Long userId);

  UserRoleCoreResponse getRoleByUserId(Long userId);
}
