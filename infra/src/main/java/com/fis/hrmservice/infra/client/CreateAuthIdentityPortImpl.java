package com.fis.hrmservice.infra.client;

import com.fis.hrmservice.domain.model.resonse.SetUserRoleCoreResponse;
import com.fis.hrmservice.domain.model.resonse.UserRoleCoreResponse;
import com.fis.hrmservice.domain.port.output.feign.CreateAuthIdentityPort;
import com.fis.hrmservice.infra.feign.client.AuthIdentityFeignClient;
import com.fis.hrmservice.infra.feign.request.AssignRoleRequest;
import com.fis.hrmservice.infra.mapper.FeignInfraMapper;
import com.fis.hrmservice.infra.model.CreateUserPassIdentityRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateAuthIdentityPortImpl implements CreateAuthIdentityPort {

  private final AuthIdentityFeignClient authIdentityFeignClient;

  private final FeignInfraMapper feignInfraMapper;

  @Override
  public void createAuthIdentity(Long userId, String email) {
    authIdentityFeignClient.createAuthIdentity(new CreateUserPassIdentityRequest(userId, email));
  }

  @Override
  public void unlockAuthIdentity(Long userId) {
    authIdentityFeignClient.unlockAuthIdentity(userId);
  }

  @Override
  public void lockAuthIdentity(Long userId) {
    authIdentityFeignClient.lockAuthIdentity(userId);
  }

  @Override
  public UserRoleCoreResponse getRoleByUserId(Long userId) {
    return feignInfraMapper.toUserRoleCoreResponse(authIdentityFeignClient.getRoleByUserId(userId));
  }

  @Override
  public void setUserRole(Long userId, Long roleId) {
    authIdentityFeignClient.setUserRole(userId, new AssignRoleRequest(roleId));
  }
}
