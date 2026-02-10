package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.constant.TicketType;
import com.fis.hrmservice.domain.model.ticket.RemoteRequestModel;
import com.fis.hrmservice.domain.model.ticket.TicketModel;
import com.fis.hrmservice.domain.model.ticket.TicketTypeModel;
import com.fis.hrmservice.domain.model.user.WorkLocationModel;
import com.fis.hrmservice.infra.persistence.entity.RemoteRequest;
import com.fis.hrmservice.infra.persistence.entity.Ticket;
import com.fis.hrmservice.infra.persistence.entity.WorkLocation;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-10T10:06:37+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class RemoteRequestMapperImpl implements RemoteRequestMapper {

    @Override
    public RemoteRequest toEntity(RemoteRequestModel model) {
        if ( model == null ) {
            return null;
        }

        RemoteRequest remoteRequest = new RemoteRequest();

        remoteRequest.setTickets( ticketModelToTicket( model.getTicket() ) );
        remoteRequest.setRemoteType( remoteTypeToString( model.getRemoteType() ) );
        remoteRequest.setEndTime( model.getEndTime() );
        remoteRequest.setStartTime( model.getStartTime() );

        return remoteRequest;
    }

    @Override
    public RemoteRequestModel toModel(RemoteRequest entity) {
        if ( entity == null ) {
            return null;
        }

        RemoteRequestModel.RemoteRequestModelBuilder remoteRequestModel = RemoteRequestModel.builder();

        remoteRequestModel.ticket( ticketToTicketModel( entity.getTickets() ) );
        remoteRequestModel.remoteType( stringToRemoteType( entity.getRemoteType() ) );
        remoteRequestModel.workLocation( workLocationToWorkLocationModel( entity.getWorkLocation() ) );
        remoteRequestModel.startTime( entity.getStartTime() );
        remoteRequestModel.endTime( entity.getEndTime() );

        return remoteRequestModel.build();
    }

    protected com.fis.hrmservice.infra.persistence.entity.TicketType ticketTypeModelToTicketType(TicketTypeModel ticketTypeModel) {
        if ( ticketTypeModel == null ) {
            return null;
        }

        com.fis.hrmservice.infra.persistence.entity.TicketType ticketType = new com.fis.hrmservice.infra.persistence.entity.TicketType();

        ticketType.setCreatedAt( ticketTypeModel.getCreatedAt() );
        if ( ticketTypeModel.getCreatedBy() != null ) {
            ticketType.setCreatedBy( Long.parseLong( ticketTypeModel.getCreatedBy() ) );
        }
        ticketType.setDescription( ticketTypeModel.getDescription() );
        if ( ticketTypeModel.getTypeName() != null ) {
            ticketType.setTypeName( ticketTypeModel.getTypeName().name() );
        }
        ticketType.setUpdatedAt( ticketTypeModel.getUpdatedAt() );
        if ( ticketTypeModel.getUpdatedBy() != null ) {
            ticketType.setUpdatedBy( Long.parseLong( ticketTypeModel.getUpdatedBy() ) );
        }

        return ticketType;
    }

    protected Ticket ticketModelToTicket(TicketModel ticketModel) {
        if ( ticketModel == null ) {
            return null;
        }

        Ticket ticket = new Ticket();

        ticket.setCreatedAt( ticketModel.getCreatedAt() );
        if ( ticketModel.getCreatedBy() != null ) {
            ticket.setCreatedBy( Long.parseLong( ticketModel.getCreatedBy() ) );
        }
        ticket.setEndAt( ticketModel.getEndAt() );
        ticket.setReason( ticketModel.getReason() );
        ticket.setStartAt( ticketModel.getStartAt() );
        ticket.setStatus( ticketModel.getStatus() );
        ticket.setTicketType( ticketTypeModelToTicketType( ticketModel.getTicketType() ) );
        ticket.setUpdatedAt( ticketModel.getUpdatedAt() );
        if ( ticketModel.getUpdatedBy() != null ) {
            ticket.setUpdatedBy( Long.parseLong( ticketModel.getUpdatedBy() ) );
        }

        return ticket;
    }

    protected TicketTypeModel ticketTypeToTicketTypeModel(com.fis.hrmservice.infra.persistence.entity.TicketType ticketType) {
        if ( ticketType == null ) {
            return null;
        }

        TicketTypeModel.TicketTypeModelBuilder ticketTypeModel = TicketTypeModel.builder();

        if ( ticketType.getTypeName() != null ) {
            ticketTypeModel.typeName( Enum.valueOf( TicketType.class, ticketType.getTypeName() ) );
        }
        ticketTypeModel.description( ticketType.getDescription() );

        return ticketTypeModel.build();
    }

    protected TicketModel ticketToTicketModel(Ticket ticket) {
        if ( ticket == null ) {
            return null;
        }

        TicketModel.TicketModelBuilder ticketModel = TicketModel.builder();

        ticketModel.ticketType( ticketTypeToTicketTypeModel( ticket.getTicketType() ) );
        ticketModel.startAt( ticket.getStartAt() );
        ticketModel.endAt( ticket.getEndAt() );
        ticketModel.reason( ticket.getReason() );

        return ticketModel.build();
    }

    protected WorkLocationModel workLocationToWorkLocationModel(WorkLocation workLocation) {
        if ( workLocation == null ) {
            return null;
        }

        WorkLocationModel.WorkLocationModelBuilder workLocationModel = WorkLocationModel.builder();

        workLocationModel.name( workLocation.getName() );
        workLocationModel.address( workLocation.getAddress() );
        workLocationModel.description( workLocation.getDescription() );
        workLocationModel.isActive( workLocation.getIsActive() );

        return workLocationModel.build();
    }
}
