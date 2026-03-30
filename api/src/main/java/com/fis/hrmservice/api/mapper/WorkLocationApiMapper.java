package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.WorkLocationRequest;
import com.fis.hrmservice.api.dto.response.WorkLocationResponse;
import com.fis.hrmservice.domain.model.user.WorkLocationModel;
import com.fis.hrmservice.domain.usecase.command.ticket.CreateWorkLocationCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.UpdateWorkLocationCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkLocationApiMapper {

    WorkLocationResponse toWorkLocationResponse(WorkLocationModel model);

    CreateWorkLocationCommand toCreateWorkLocationCommand(WorkLocationRequest request);

    UpdateWorkLocationCommand toUpdateWorkLocationCommand(WorkLocationRequest request);
}
