package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.CreateTicketRequest;
import com.fis.hrmservice.api.dto.request.FilterRegistrationRequest;
import com.fis.hrmservice.api.dto.request.RemoteTicketRequest;
import com.fis.hrmservice.api.dto.response.FirstThreeRegistrationResponse;
import com.fis.hrmservice.api.dto.response.ListRegistrationResponse;
import com.fis.hrmservice.api.dto.response.RegistrationDetailResponse;
import com.fis.hrmservice.api.dto.response.TicketResponse;
import com.fis.hrmservice.domain.model.constant.RemoteType;
import com.fis.hrmservice.domain.model.constant.TicketStatus;
import com.fis.hrmservice.domain.model.ticket.LeaveRequestModel;
import com.fis.hrmservice.domain.model.ticket.RemoteRequestModel;
import com.fis.hrmservice.domain.model.ticket.TicketModel;
import com.fis.hrmservice.domain.usecase.command.ticket.CreateTicketCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.FilterRegistrationTicketCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.RemoteRequestCommand;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TicketApiMapper {

  CreateTicketRequest toTicketRequest(CreateTicketCommand command);

  CreateTicketCommand toTicketCommand(CreateTicketRequest request);

  @Mapping(source = "ticket.ticketId", target = "ticketId")
  @Mapping(source = "ticket.ticketType.typeName", target = "ticketType")
  @Mapping(source = "ticket.sysStatus", target = "ticketStatus")
  @Mapping(source = "ticket.startAt", target = "createDate")
  TicketResponse toTicketResponse(LeaveRequestModel leaveModel);

  @Mapping(source = "ticketId", target = "ticketId")
  @Mapping(source = "ticketType.typeName", target = "ticketType")
  @Mapping(source = "sysStatus", target = "ticketStatus")
  @Mapping(source = "startAt", target = "createDate")
  TicketResponse toTicketResponse(TicketModel ticketModel);

  @Mapping(source = "ticket.ticketId", target = "ticketId")
  @Mapping(source = "remoteType", target = "ticketType")
  @Mapping(source = "ticket.sysStatus", target = "ticketStatus")
  @Mapping(source = "ticket.startAt", target = "createDate")
  TicketResponse toTicketResponse(RemoteRequestModel requestModel);

  default String map(RemoteType type) {
    return type == null ? null : type.name();
  }

  default String map(TicketStatus status) {
    return status == null ? null : status.name();
  }

  RemoteRequestCommand toRemoteRequestCommand(RemoteTicketRequest leaveRequest);

  @Mappings({
    // User info
    @Mapping(target = "fullName", source = "requester.fullName"),
    @Mapping(target = "companyEmail", source = "requester.companyEmail"),

    // Department (tạm assume từ position)
    @Mapping(
        target = "departmentName",
        ignore = true /*source = "requester.position.department.departmentName" */),

    // Ticket info
    @Mapping(
        target = "ticketTypeName",
        expression = "java(ticketModel.getTicketType().getTypeName().name())"),
    @Mapping(target = "ticketStatus", expression = "java(ticketModel.getSysStatus().name())"),

    // no thường set ở service layer
    @Mapping(target = "no", ignore = true)
  })
  ListRegistrationResponse toRegistrationResponse(TicketModel ticketModel);

  @Mappings({
    @Mapping(target = "senderFullName", source = "requester.fullName"),
    @Mapping(target = "registrationDate", source = "startAt"),
    @Mapping(target = "ticketStatus", expression = "java(ticketModel.getSysStatus().name())")
  })
  FirstThreeRegistrationResponse toFirstThreeRegistrationResponse(TicketModel ticketModel);

  @Mappings({
    @Mapping(target = "avatarUrl", source = "requester.avatar.avatarUrl"),
    @Mapping(target = "positionName", source = "requester.position.name"),
    @Mapping(target = "cvUrl", source = "requester.cv.cvUrl"),
    @Mapping(target = "internshipStartDate", source = "requester.internshipStartDate"),
    @Mapping(target = "internshipEndDate", source = "requester.internshipEndDate"),
    @Mapping(target = "fullName", source = "requester.fullName"),
    @Mapping(target = "idNumber", source = "requester.idNumber"),
    @Mapping(target = "dateOfBirth", source = "requester.dateOfBirth"),
    @Mapping(target = "phoneNumber", source = "requester.phoneNumber"),
    @Mapping(target = "companyEmail", source = "requester.companyEmail"),
    @Mapping(target = "address", source = "requester.address"),
    @Mapping(target = "sysStatus", source = "sysStatus")
  })
  RegistrationDetailResponse toRegistrationDetailResponse(TicketModel ticketModel);

  // ===== Filter Registration Ticket =====
  @Mapping(target = "ticketStatus", expression = "java(toTicketStatus(request.getTicketStatus()))")
  FilterRegistrationTicketCommand toCommand(FilterRegistrationRequest request);

  default TicketStatus toTicketStatus(String ticketStatus) {
    if (ticketStatus == null || ticketStatus.isBlank()) return null;
    try {
      return TicketStatus.valueOf(ticketStatus.toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  List<ListRegistrationResponse> toRegistrationResponseList(List<TicketModel> ticketModels);
}
