package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.ticket.TicketModel;
import com.fis.hrmservice.infra.persistence.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(
        componentModel = "spring",
        uses = {
                TicketTypeMapper.class,
                UserMapper.class
        }
)
public interface TicketMapper {

    /* ENTITY → MODEL */

    @Mapping(source = "id", target = "ticketId")
    @Mapping(source = "user", target = "requester")
    @Mapping(source = "status", target = "sysStatus")
    TicketModel toModel(Ticket ticket);

    /* MODEL → ENTITY */

    @Mapping(source = "ticketId", target = "id")
    @Mapping(source = "requester", target = "user")
    @Mapping(source = "sysStatus", target = "status")
    @Mapping(source = "ticketType", target = "ticketType")   // 🔥 THIẾU DÒNG NÀY

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Ticket toEntity(TicketModel ticketModel);
}