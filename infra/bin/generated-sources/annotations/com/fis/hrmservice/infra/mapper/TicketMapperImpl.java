package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.constant.TicketStatus;
import com.fis.hrmservice.domain.model.ticket.TicketModel;
import com.fis.hrmservice.infra.persistence.entity.Ticket;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-10T10:06:37+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class TicketMapperImpl implements TicketMapper {

    @Autowired
    private TicketTypeMapper ticketTypeMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public TicketModel toModel(Ticket ticket) {
        if ( ticket == null ) {
            return null;
        }

        TicketModel.TicketModelBuilder ticketModel = TicketModel.builder();

        ticketModel.ticketId( ticket.getId() );
        ticketModel.requester( userMapper.toModel( ticket.getUser() ) );
        if ( ticket.getStatus() != null ) {
            ticketModel.sysStatus( Enum.valueOf( TicketStatus.class, ticket.getStatus() ) );
        }
        ticketModel.ticketType( ticketTypeMapper.toModel( ticket.getTicketType() ) );
        ticketModel.startAt( ticket.getStartAt() );
        ticketModel.endAt( ticket.getEndAt() );
        ticketModel.reason( ticket.getReason() );

        return ticketModel.build();
    }

    @Override
    public Ticket toEntity(TicketModel ticketModel) {
        if ( ticketModel == null ) {
            return null;
        }

        Ticket ticket = new Ticket();

        ticket.setId( ticketModel.getTicketId() );
        ticket.setUser( userMapper.toEntity( ticketModel.getRequester() ) );
        if ( ticketModel.getSysStatus() != null ) {
            ticket.setStatus( ticketModel.getSysStatus().name() );
        }
        ticket.setTicketType( ticketTypeMapper.toEntity( ticketModel.getTicketType() ) );
        ticket.setEndAt( ticketModel.getEndAt() );
        ticket.setReason( ticketModel.getReason() );
        ticket.setStartAt( ticketModel.getStartAt() );

        return ticket;
    }
}
