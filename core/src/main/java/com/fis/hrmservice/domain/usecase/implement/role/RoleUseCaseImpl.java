package com.fis.hrmservice.domain.usecase.implement.role;

import com.fis.hrmservice.domain.model.dto.resonse.ListRoleCoreResponse;
import com.fis.hrmservice.domain.port.output.feign.CreateAuthIdentityPort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleUseCaseImpl {

  CreateAuthIdentityPort createAuthIdentityPort;

  public List<ListRoleCoreResponse> getAllRoles() {
    return createAuthIdentityPort.getAllRoles();
  }
}