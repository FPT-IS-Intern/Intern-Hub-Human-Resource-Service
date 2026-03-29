package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.ticket.TicketTypeModel;
import com.fis.hrmservice.infra.persistence.entity.TicketType;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-29T16:00:35+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class TicketTypeMapperImpl implements TicketTypeMapper {

    @Override
    public TicketTypeModel toModel(TicketType entity) {
        if ( entity == null ) {
            return null;
        }

        TicketTypeModel.TicketTypeModelBuilder ticketTypeModel = TicketTypeModel.builder();

        ticketTypeModel.ticketTypeId( entity.getId() );
        ticketTypeModel.description( entity.getDescription() );

        ticketTypeModel.typeName( mapToEnum(entity.getTypeName()) );

        return ticketTypeModel.build();
    }

    @Override
    public TicketType toEntity(TicketTypeModel model) {
        if ( model == null ) {
            return null;
        }

        TicketType ticketType = new TicketType();

        ticketType.setId( model.getTicketTypeId() );
        ticketType.setCreatedAt( model.getCreatedAt() );
        if ( model.getCreatedBy() != null ) {
            ticketType.setCreatedBy( Long.parseLong( model.getCreatedBy() ) );
        }
        ticketType.setDescription( model.getDescription() );
        ticketType.setUpdatedAt( model.getUpdatedAt() );
        if ( model.getUpdatedBy() != null ) {
            ticketType.setUpdatedBy( Long.parseLong( model.getUpdatedBy() ) );
        }

        ticketType.setTypeName( mapToString(model.getTypeName()) );

        return ticketType;
    }
}
