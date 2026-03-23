package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.resonse.ListRoleCoreResponse;
import com.fis.hrmservice.domain.model.resonse.SetUserRoleCoreResponse;
import com.fis.hrmservice.domain.model.resonse.UserRoleCoreResponse;
import com.fis.hrmservice.infra.feign.response.ListRoleInfraResponse;
import com.fis.hrmservice.infra.feign.response.SetUserRoleResponse;
import com.fis.hrmservice.infra.feign.response.UserRoleInfraResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-22T17:23:38+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
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
}
