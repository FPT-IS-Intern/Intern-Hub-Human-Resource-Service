package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.WorkLocationRequest;
import com.fis.hrmservice.api.dto.response.WorkLocationResponse;
import com.fis.hrmservice.domain.model.user.WorkLocationModel;
import com.fis.hrmservice.domain.usecase.command.ticket.CreateWorkLocationCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.UpdateWorkLocationCommand;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-01T22:49:53+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class WorkLocationApiMapperImpl implements WorkLocationApiMapper {

    @Override
    public WorkLocationResponse toWorkLocationResponse(WorkLocationModel model) {
        if ( model == null ) {
            return null;
        }

        WorkLocationResponse.WorkLocationResponseBuilder workLocationResponse = WorkLocationResponse.builder();

        workLocationResponse.address( model.getAddress() );
        workLocationResponse.description( model.getDescription() );
        workLocationResponse.isActive( model.getIsActive() );
        workLocationResponse.name( model.getName() );
        workLocationResponse.workLocationId( model.getWorkLocationId() );

        return workLocationResponse.build();
    }

    @Override
    public CreateWorkLocationCommand toCreateWorkLocationCommand(WorkLocationRequest request) {
        if ( request == null ) {
            return null;
        }

        CreateWorkLocationCommand createWorkLocationCommand = new CreateWorkLocationCommand();

        createWorkLocationCommand.setName( request.getName() );
        createWorkLocationCommand.setAddress( request.getAddress() );
        createWorkLocationCommand.setDescription( request.getDescription() );

        return createWorkLocationCommand;
    }

    @Override
    public UpdateWorkLocationCommand toUpdateWorkLocationCommand(WorkLocationRequest request) {
        if ( request == null ) {
            return null;
        }

        UpdateWorkLocationCommand updateWorkLocationCommand = new UpdateWorkLocationCommand();

        updateWorkLocationCommand.setName( request.getName() );
        updateWorkLocationCommand.setAddress( request.getAddress() );
        updateWorkLocationCommand.setDescription( request.getDescription() );

        return updateWorkLocationCommand;
    }
}
