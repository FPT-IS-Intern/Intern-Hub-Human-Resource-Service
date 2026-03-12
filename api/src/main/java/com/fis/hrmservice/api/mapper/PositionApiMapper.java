package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.AddPositionRequest;
import com.fis.hrmservice.api.dto.response.PositionListResponse;
import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.usecase.command.user.AddPositionUseCaseCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PositionApiMapper {

  PositionListResponse toPositionListResponse(PositionModel model);

  AddPositionUseCaseCommand toAddPositionCommand(AddPositionRequest addPositionRequest);
}
