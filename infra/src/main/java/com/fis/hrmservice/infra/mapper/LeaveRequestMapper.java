package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.ticket.LeaveRequestModel;
import com.fis.hrmservice.infra.persistence.entity.LeaveRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LeaveRequestMapper {

  LeaveRequestModel toModel(LeaveRequest leaveRequest);

  LeaveRequest toEntity(LeaveRequestModel leaveRequestModel);
}
