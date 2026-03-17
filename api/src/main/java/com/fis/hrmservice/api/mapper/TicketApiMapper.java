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
            @Mapping(target = "fullName", expression = "java(readString(ticketModel.getUserInfoTemp(), \"fullName\"))"),
            @Mapping(target = "companyEmail", expression = "java(readString(ticketModel.getUserInfoTemp(), \"companyEmail\"))"),
            @Mapping(target = "departmentName", ignore = true),
            @Mapping(target = "ticketTypeName", expression = "java(ticketModel.getTicketType().getTypeName().name())"),
            @Mapping(target = "ticketStatus", expression = "java(ticketModel.getSysStatus().name())"),
            @Mapping(target = "no", ignore = true)
    })
    ListRegistrationResponse toRegistrationResponse(TicketModel ticketModel);

    default FirstThreeRegistrationResponse toFirstThreeRegistrationResponse(TicketModel ticketModel) {
        if (ticketModel == null) {
            return null;
        }

        String senderFullName = String.valueOf(ticketModel.getUserInfoTemp().get("fullName"));

        return FirstThreeRegistrationResponse.builder()
                .senderFullName(senderFullName)
                .registrationDate(ticketModel.getStartAt())
                .ticketStatus(ticketModel.getSysStatus() == null ? null : ticketModel.getSysStatus().name())
                .build();
    }

    @Mappings({
            @Mapping(target = "userId", expression = "java(readString(ticketModel.getUserInfoTemp(), \"userId\"))"),
            @Mapping(target = "avatarUrl", expression = "java(readString(ticketModel.getUserInfoTemp(), \"avatarUrl\"))"),
            @Mapping(target = "positionName", expression = "java(readString(ticketModel.getUserInfoTemp(), \"positionCode\"))"),
            @Mapping(target = "cvUrl", expression = "java(readString(ticketModel.getUserInfoTemp(), \"cvUrl\"))"),
            @Mapping(target = "internshipStartDate", expression = "java(readLocalDate(ticketModel.getUserInfoTemp(), \"internshipStartDate\"))"),
            @Mapping(target = "internshipEndDate", expression = "java(readLocalDate(ticketModel.getUserInfoTemp(), \"internshipEndDate\"))"),
            @Mapping(target = "fullName", expression = "java(readString(ticketModel.getUserInfoTemp(), \"fullName\"))"),
            @Mapping(target = "idNumber", expression = "java(readString(ticketModel.getUserInfoTemp(), \"idNumber\"))"),
            @Mapping(target = "dateOfBirth", expression = "java(readLocalDate(ticketModel.getUserInfoTemp(), \"dateOfBirth\"))"),
            @Mapping(target = "phoneNumber", expression = "java(readString(ticketModel.getUserInfoTemp(), \"phoneNumber\"))"),
            @Mapping(target = "companyEmail", expression = "java(readString(ticketModel.getUserInfoTemp(), \"companyEmail\"))"),
            @Mapping(target = "address", expression = "java(readString(ticketModel.getUserInfoTemp(), \"address\"))"),
            @Mapping(target = "sysStatus", source = "sysStatus")
    })
    RegistrationDetailResponse toRegistrationDetailResponse(TicketModel ticketModel);

    default String readString(java.util.Map<String, Object> source, String key) {
        if (source == null || source.get(key) == null) return null;
        return String.valueOf(source.get(key));
    }

    default java.time.LocalDate readLocalDate(java.util.Map<String, Object> source, String key) {
        if (source == null || source.get(key) == null) return null;
        try {
            return java.time.LocalDate.parse(String.valueOf(source.get(key)));
        } catch (Exception e) {
            return null;
        }
    }

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
