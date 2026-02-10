package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.constant.TicketType;
import com.fis.hrmservice.domain.model.ticket.TicketTypeModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-10T10:06:37+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class TicketTypeMapperImpl implements TicketTypeMapper {

    @Override
    public TicketTypeModel toModel(com.fis.hrmservice.infra.persistence.entity.TicketType entity) {
        if ( entity == null ) {
            return null;
        }

        TicketTypeModel.TicketTypeModelBuilder ticketTypeModel = TicketTypeModel.builder();

        ticketTypeModel.ticketTypeId( entity.getId() );
        if ( entity.getTypeName() != null ) {
            ticketTypeModel.typeName( Enum.valueOf( TicketType.class, entity.getTypeName() ) );
        }
        ticketTypeModel.description( entity.getDescription() );

        return ticketTypeModel.build();
    }

    @Override
    public com.fis.hrmservice.infra.persistence.entity.TicketType toEntity(TicketTypeModel model) {
        if ( model == null ) {
            return null;
        }

        com.fis.hrmservice.infra.persistence.entity.TicketType ticketType = new com.fis.hrmservice.infra.persistence.entity.TicketType();

        ticketType.setId( model.getTicketTypeId() );
        ticketType.setCreatedAt( model.getCreatedAt() );
        if ( model.getCreatedBy() != null ) {
            ticketType.setCreatedBy( Long.parseLong( model.getCreatedBy() ) );
        }
        ticketType.setDescription( model.getDescription() );
        if ( model.getTypeName() != null ) {
            ticketType.setTypeName( model.getTypeName().name() );
        }
        ticketType.setUpdatedAt( model.getUpdatedAt() );
        if ( model.getUpdatedBy() != null ) {
            ticketType.setUpdatedBy( Long.parseLong( model.getUpdatedBy() ) );
        }

        return ticketType;
    }
}
