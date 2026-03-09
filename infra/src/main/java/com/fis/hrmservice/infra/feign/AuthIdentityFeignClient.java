package com.fis.hrmservice.infra.feign;

import com.fis.hrmservice.infra.model.CreateUserPassIdentityRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth", url = "${feign.client.config.auth.url}")
public interface AuthIdentityFeignClient {

  @PostMapping("/auth/internal/identities/userpass")
  void createAuthIdentity(@RequestBody CreateUserPassIdentityRequest request);

  @PostMapping("auth/internal/identity/lock/{userId}")
  void lockAuthIdentity(@PathVariable Long userId);

  @PostMapping("auth/internal/identity/un-lock/{userId}")
  void unlockAuthIdentity(@PathVariable Long userId);

  @PostMapping("/auth/authz/roles/by-user/{userId}")
  UserRoleInfraResponse getRoleByUserId(@PathVariable Long userId);
}
