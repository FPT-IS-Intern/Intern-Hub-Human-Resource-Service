package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.resonse.AuthIdentityStatusCoreResponse;
import com.fis.hrmservice.domain.model.resonse.ListRoleCoreResponse;
import com.fis.hrmservice.domain.model.resonse.SetUserRoleCoreResponse;
import com.fis.hrmservice.domain.model.resonse.UserRoleCoreResponse;
import com.fis.hrmservice.infra.feign.response.AuthIdentityStatusInfraResponse;
import com.fis.hrmservice.infra.feign.response.ListRoleInfraResponse;
import com.fis.hrmservice.infra.feign.response.SetUserRoleResponse;
import com.fis.hrmservice.infra.feign.response.UserRoleInfraResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FeignInfraMapper {

    UserRoleCoreResponse toUserRoleCoreResponse(UserRoleInfraResponse userRoleInfraResponse);

    SetUserRoleCoreResponse toSetUserRoleCoreResponse(SetUserRoleResponse setUserRoleResponse);

    ListRoleCoreResponse toListRoleCoreResponse(ListRoleInfraResponse listRoleInfraResponse);

    AuthIdentityStatusCoreResponse toAuthIdentityStatusCoreResponse(AuthIdentityStatusInfraResponse response);
}
