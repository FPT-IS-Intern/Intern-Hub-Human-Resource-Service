package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.resonse.UserRoleCoreResponse;
import com.fis.hrmservice.infra.feign.UserRoleInfraResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FeignInfraMapper {

    UserRoleCoreResponse toUserRoleCoreResponse(UserRoleInfraResponse userRoleInfraResponse);
}
