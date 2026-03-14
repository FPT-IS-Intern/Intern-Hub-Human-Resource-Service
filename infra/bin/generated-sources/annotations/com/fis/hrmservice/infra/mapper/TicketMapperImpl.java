package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.ticket.TicketModel;
import com.fis.hrmservice.infra.persistence.entity.Ticket;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-14T15:33:03+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
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
        ticketModel.sysStatus( ticket.getStatus() );
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
        ticket.setStatus( ticketModel.getSysStatus() );
        ticket.setTicketType( ticketTypeMapper.toEntity( ticketModel.getTicketType() ) );
        ticket.setEndAt( ticketModel.getEndAt() );
        ticket.setReason( ticketModel.getReason() );
        ticket.setStartAt( ticketModel.getStartAt() );

        return ticket;
    }
}
