package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.response.RoleListResponse;
import com.fis.hrmservice.domain.model.dto.resonse.ListRoleCoreResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleApiMapper {

  RoleListResponse toRoleListResponse(ListRoleCoreResponse model);
}

