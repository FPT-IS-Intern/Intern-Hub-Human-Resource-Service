package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.ticket.TicketTypeModel;
import com.fis.hrmservice.infra.persistence.entity.TicketType;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TicketTypeMapper {

  TicketTypeModel toModel(TicketType ticketType);

  TicketType toEntity(TicketTypeModel ticketTypeModel);
}
