package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.ticket.TicketModel;
import com.fis.hrmservice.infra.persistence.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
      TicketTypeMapper.class,
      UserMapper.class // nếu có User ↔ UserModel
    })
public interface TicketMapper {

  @Mapping(source = "id", target = "ticketId")
  @Mapping(source = "user", target = "requester")
  @Mapping(source = "ticketType", target = "ticketType")
  @Mapping(source = "startAt", target = "startAt")
  @Mapping(source = "endAt", target = "endAt")
  @Mapping(source = "reason", target = "reason")
  @Mapping(source = "status", target = "sysStatus")
  TicketModel toModel(Ticket ticket);

  @Mapping(source = "ticketId", target = "id")
  @Mapping(source = "requester", target = "user")
  @Mapping(source = "ticketType", target = "ticketType")
  @Mapping(source = "startAt", target = "startAt")
  @Mapping(source = "endAt", target = "endAt")
  @Mapping(source = "reason", target = "reason")
  @Mapping(source = "sysStatus", target = "status")
  Ticket toEntity(TicketModel ticketModel);
}
