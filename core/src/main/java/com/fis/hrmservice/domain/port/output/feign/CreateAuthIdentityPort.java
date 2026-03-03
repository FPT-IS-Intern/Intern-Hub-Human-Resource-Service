package com.fis.hrmservice.domain.port.output.feign;

public interface CreateAuthIdentityPort {

  void createAuthIdentity(Long userId, String email);
}
