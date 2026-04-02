package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.user.WorkLocationModel;
import com.fis.hrmservice.infra.persistence.entity.WorkLocation;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-01T22:49:48+0700",
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

        WorkLocation.WorkLocationBuilder workLocation = WorkLocation.builder();

        workLocation.id( model.getWorkLocationId() );
        workLocation.address( model.getAddress() );
        workLocation.description( model.getDescription() );
        workLocation.isActive( model.getIsActive() );
        workLocation.name( model.getName() );

        return workLocation.build();
    }
}
