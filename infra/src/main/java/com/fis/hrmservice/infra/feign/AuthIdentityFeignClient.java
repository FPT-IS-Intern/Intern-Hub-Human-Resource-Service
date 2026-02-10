package com.fis.hrmservice.infra.feign;

import com.fis.hrmservice.infra.model.CreateUserPassIdentityRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth")
public interface AuthIdentityFeignClient {

  @PostMapping("/auth/internal/identities/userpass")
  void createAuthIdentity(@RequestBody CreateUserPassIdentityRequest request);
}
