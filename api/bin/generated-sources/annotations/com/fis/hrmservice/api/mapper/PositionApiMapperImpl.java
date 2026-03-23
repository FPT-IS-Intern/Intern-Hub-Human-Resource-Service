package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.AddPositionRequest;
import com.fis.hrmservice.api.dto.response.PositionListResponse;
import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.usecase.command.user.AddPositionUseCaseCommand;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-22T17:23:39+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class PositionApiMapperImpl implements PositionApiMapper {

    @Override
    public PositionListResponse toPositionListResponse(PositionModel model) {
        if ( model == null ) {
            return null;
        }

        PositionListResponse positionListResponse = new PositionListResponse();

        positionListResponse.setName( model.getName() );
        positionListResponse.setPositionId( model.getPositionId() );

        return positionListResponse;
    }

    @Override
    public AddPositionUseCaseCommand toAddPositionCommand(AddPositionRequest addPositionRequest) {
        if ( addPositionRequest == null ) {
            return null;
        }

        AddPositionUseCaseCommand addPositionUseCaseCommand = new AddPositionUseCaseCommand();

        addPositionUseCaseCommand.setName( addPositionRequest.getName() );
        addPositionUseCaseCommand.setDescription( addPositionRequest.getDescription() );

        return addPositionUseCaseCommand;
    }
}
