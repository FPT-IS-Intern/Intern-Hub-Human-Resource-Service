package com.fis.hrmservice.infra.feign;

import com.fis.hrmservice.infra.model.CreateUserPassIdentityRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth", url = "${feign.client.config.auth.url}")
public interface AuthIdentityFeignClient {

  @PostMapping("/auth/internal/identities/userpass")
  void createAuthIdentity(@RequestBody CreateUserPassIdentityRequest request);

  @PostMapping("auth/internal/identity/lock/{userId}")
  void lockAuthIdentity(@Param("userId") Long userId);

  @PostMapping("auth/internal/identity/un-lock/{userId}")
  void unlockAuthIdentity(@Param("userId") Long userId);
}
