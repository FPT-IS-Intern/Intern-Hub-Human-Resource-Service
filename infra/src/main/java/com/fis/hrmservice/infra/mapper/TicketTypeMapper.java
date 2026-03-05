package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.ticket.TicketTypeModel;
import com.fis.hrmservice.infra.persistence.entity.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketTypeMapper {

  @Mapping(source = "id", target = "ticketTypeId")
  @Mapping(target = "typeName", expression = "java(mapToEnum(entity.getTypeName()))")
  TicketTypeModel toModel(TicketType entity);

  @Mapping(source = "ticketTypeId", target = "id")
  @Mapping(target = "typeName", expression = "java(mapToString(model.getTypeName()))")
  TicketType toEntity(TicketTypeModel model);

  default com.fis.hrmservice.domain.model.constant.TicketType mapToEnum(String value) {
    if (value == null) return null;

    try {
      return com.fis.hrmservice.domain.model.constant.TicketType.valueOf(
          value.trim().toUpperCase().replace(" ", "_"));
    } catch (Exception e) {
      throw new IllegalStateException("Invalid TicketType in DB: " + value);
    }
  }

  default String mapToString(com.fis.hrmservice.domain.model.constant.TicketType type) {

    return type != null ? type.name() : null;
  }
}
