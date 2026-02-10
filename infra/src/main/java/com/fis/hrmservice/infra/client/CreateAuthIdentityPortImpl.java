package com.fis.hrmservice.infra.client;

import com.fis.hrmservice.domain.port.output.CreateAuthIdentityPort;
import com.fis.hrmservice.infra.feign.AuthIdentityFeignClient;
import com.fis.hrmservice.infra.model.CreateUserPassIdentityRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateAuthIdentityPortImpl implements CreateAuthIdentityPort {

  private final AuthIdentityFeignClient authIdentityFeignClient;

  @Override
  public void createAuthIdentity(Long userId, String email) {
    authIdentityFeignClient.createAuthIdentity(new CreateUserPassIdentityRequest(userId, email));
  }
}
