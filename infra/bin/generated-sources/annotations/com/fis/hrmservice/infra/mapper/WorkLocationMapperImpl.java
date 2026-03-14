package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.user.WorkLocationModel;
import com.fis.hrmservice.infra.persistence.entity.WorkLocation;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-14T15:33:02+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class WorkLocationMapperImpl implements WorkLocationMapper {

    @Override
    public WorkLocationModel toModel(WorkLocation entity) {
        if ( entity == null ) {
            return null;
        }

        WorkLocationModel.WorkLocationModelBuilder workLocationModel = WorkLocationModel.builder();

        workLocationModel.workLocationId( entity.getId() );
        workLocationModel.name( entity.getName() );
        workLocationModel.address( entity.getAddress() );
        workLocationModel.description( entity.getDescription() );
        workLocationModel.isActive( entity.getIsActive() );

        return workLocationModel.build();
    }

    @Override
    public WorkLocation toEntity(WorkLocationModel model) {
        if ( model == null ) {
            return null;
        }

        WorkLocation workLocation = new WorkLocation();

        workLocation.setId( model.getWorkLocationId() );
        workLocation.setCreatedAt( model.getCreatedAt() );
        workLocation.setUpdatedAt( model.getUpdatedAt() );
        if ( model.getCreatedBy() != null ) {
            workLocation.setCreatedBy( Long.parseLong( model.getCreatedBy() ) );
        }
        if ( model.getUpdatedBy() != null ) {
            workLocation.setUpdatedBy( Long.parseLong( model.getUpdatedBy() ) );
        }
        workLocation.setAddress( model.getAddress() );
        workLocation.setDescription( model.getDescription() );
        workLocation.setIsActive( model.getIsActive() );
        workLocation.setName( model.getName() );

        return workLocation;
    }
}
