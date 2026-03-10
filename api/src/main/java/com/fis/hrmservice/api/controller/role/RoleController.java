package com.fis.hrmservice.api.controller.role;

import com.fis.hrmservice.api.dto.response.RoleListResponse;
import com.fis.hrmservice.api.mapper.RoleApiMapper;
import com.fis.hrmservice.domain.usecase.implement.role.RoleUseCaseImpl;
import com.intern.hub.library.common.dto.ResponseApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("hrm/users/roles")
@Tag(name = "Role Management", description = "APIs for role management")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

  RoleUseCaseImpl roleUseCase;

  RoleApiMapper roleApiMapper;

  @GetMapping
  public ResponseApi<List<RoleListResponse>> listAllRoles() {
    return ResponseApi.ok(
        roleUseCase.getAllRoles().stream()
            .map(roleApiMapper::toRoleListResponse)
            .toList());
  }
}

