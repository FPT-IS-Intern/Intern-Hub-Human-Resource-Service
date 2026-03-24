package com.fis.hrmservice.infra.feign.client;

import com.fis.hrmservice.infra.feign.request.AssignRoleRequest;
import com.fis.hrmservice.infra.feign.response.AuthIdentityStatusInfraResponse;
import com.fis.hrmservice.infra.feign.response.ListRoleInfraResponse;
import com.fis.hrmservice.infra.feign.response.SetUserRoleResponse;
import com.fis.hrmservice.infra.feign.response.UserRoleInfraResponse;
import com.fis.hrmservice.infra.model.CreateUserPassIdentityRequest;
import com.intern.hub.library.common.dto.ResponseApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "auth", url = "${feign.client.config.auth.url}")
public interface AuthIdentityFeignClient {

  @PostMapping("/auth/internal/identities/userpass")
  void createAuthIdentity(@RequestBody CreateUserPassIdentityRequest request);

  @PutMapping("/auth/internal/identity/lock/{userId}")
  void lockAuthIdentity(@PathVariable Long userId);

  @PutMapping("/auth/internal/identity/unlock/{userId}")
  void unlockAuthIdentity(@PathVariable Long userId);

  @GetMapping("/auth/internal/authz/roles/by-user/{userId}")
  ResponseApi<UserRoleInfraResponse> getRoleByUserId(@PathVariable Long userId);

  @PostMapping("/auth/authz/users/{userId}/role")
  void setUserRole(@PathVariable Long userId, @RequestBody AssignRoleRequest request);

  @GetMapping("/auth/internal/authz/roles")
  ResponseApi<List<ListRoleInfraResponse>> getAllRoles();

  @PostMapping("/auth/internal/identity/statuses")
  ResponseApi<List<AuthIdentityStatusInfraResponse>> getIdentityStatuses(@RequestBody List<Long> userIds);
}
