package com.fis.hrmservice.domain.port.output;

public interface CreateAuthIdentityPort {

  void createAuthIdentity(Long userId, String email);
}
