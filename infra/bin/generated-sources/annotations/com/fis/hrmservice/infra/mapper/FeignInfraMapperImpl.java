package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.dto.resonse.AuthIdentityStatusCoreResponse;
import com.fis.hrmservice.domain.model.dto.resonse.ListRoleCoreResponse;
import com.fis.hrmservice.domain.model.dto.resonse.SetUserRoleCoreResponse;
import com.fis.hrmservice.domain.model.dto.resonse.UserRoleCoreResponse;
import com.fis.hrmservice.infra.feign.response.AuthIdentityStatusInfraResponse;
import com.fis.hrmservice.infra.feign.response.ListRoleInfraResponse;
import com.fis.hrmservice.infra.feign.response.SetUserRoleResponse;
import com.fis.hrmservice.infra.feign.response.UserRoleInfraResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-29T13:05:03+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class FeignInfraMapperImpl implements FeignInfraMapper {

    @Override
    public UserRoleCoreResponse toUserRoleCoreResponse(UserRoleInfraResponse userRoleInfraResponse) {
        if ( userRoleInfraResponse == null ) {
            return null;
        }

        UserRoleCoreResponse.UserRoleCoreResponseBuilder userRoleCoreResponse = UserRoleCoreResponse.builder();

        userRoleCoreResponse.id( userRoleInfraResponse.getId() );
        userRoleCoreResponse.name( userRoleInfraResponse.getName() );
        userRoleCoreResponse.description( userRoleInfraResponse.getDescription() );
        userRoleCoreResponse.status( userRoleInfraResponse.getStatus() );

        return userRoleCoreResponse.build();
    }

    @Override
    public SetUserRoleCoreResponse toSetUserRoleCoreResponse(SetUserRoleResponse setUserRoleResponse) {
        if ( setUserRoleResponse == null ) {
            return null;
        }

        SetUserRoleCoreResponse.SetUserRoleCoreResponseBuilder setUserRoleCoreResponse = SetUserRoleCoreResponse.builder();

        setUserRoleCoreResponse.field( setUserRoleResponse.getField() );
        setUserRoleCoreResponse.message( setUserRoleResponse.getMessage() );

        return setUserRoleCoreResponse.build();
    }

    @Override
    public ListRoleCoreResponse toListRoleCoreResponse(ListRoleInfraResponse listRoleInfraResponse) {
        if ( listRoleInfraResponse == null ) {
            return null;
        }

        ListRoleCoreResponse.ListRoleCoreResponseBuilder listRoleCoreResponse = ListRoleCoreResponse.builder();

        listRoleCoreResponse.id( listRoleInfraResponse.getId() );
        listRoleCoreResponse.name( listRoleInfraResponse.getName() );

        return listRoleCoreResponse.build();
    }

    @Override
    public AuthIdentityStatusCoreResponse toAuthIdentityStatusCoreResponse(AuthIdentityStatusInfraResponse response) {
        if ( response == null ) {
            return null;
        }

        Long userId = null;
        String sysStatus = null;

        userId = response.userId();
        sysStatus = response.sysStatus();

        AuthIdentityStatusCoreResponse authIdentityStatusCoreResponse = new AuthIdentityStatusCoreResponse( userId, sysStatus );

        return authIdentityStatusCoreResponse;
    }
}
