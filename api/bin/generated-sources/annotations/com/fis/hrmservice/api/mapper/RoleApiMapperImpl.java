package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.response.RoleListResponse;
import com.fis.hrmservice.domain.model.dto.resonse.ListRoleCoreResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-29T16:00:39+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class RoleApiMapperImpl implements RoleApiMapper {

    @Override
    public RoleListResponse toRoleListResponse(ListRoleCoreResponse model) {
        if ( model == null ) {
            return null;
        }

        RoleListResponse roleListResponse = new RoleListResponse();

        roleListResponse.setId( model.getId() );
        roleListResponse.setName( model.getName() );

        return roleListResponse;
    }
}
