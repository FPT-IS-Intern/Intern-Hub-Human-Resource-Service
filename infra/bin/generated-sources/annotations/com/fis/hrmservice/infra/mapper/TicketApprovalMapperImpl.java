package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.ticket.TicketApprovalModel;
import com.fis.hrmservice.infra.persistence.entity.TicketApproval;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-10T10:06:37+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class TicketApprovalMapperImpl implements TicketApprovalMapper {

    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public TicketApprovalModel toModel(TicketApproval entity) {
        if ( entity == null ) {
            return null;
        }

        TicketApprovalModel.TicketApprovalModelBuilder<?, ?> ticketApprovalModel = TicketApprovalModel.builder();

        ticketApprovalModel.approvalId( entity.getId() );
        if ( entity.getCreatedAt() != null ) {
            ticketApprovalModel.createdAt( entity.getCreatedAt() );
        }
        if ( entity.getUpdatedAt() != null ) {
            ticketApprovalModel.updatedAt( entity.getUpdatedAt() );
        }
        ticketApprovalModel.version( entity.getVersion() );
        ticketApprovalModel.status( entity.getStatus() );
        if ( entity.getCreatedBy() != null ) {
            ticketApprovalModel.createdBy( String.valueOf( entity.getCreatedBy() ) );
        }
        if ( entity.getUpdatedBy() != null ) {
            ticketApprovalModel.updatedBy( String.valueOf( entity.getUpdatedBy() ) );
        }
        ticketApprovalModel.ticket( ticketMapper.toModel( entity.getTicket() ) );
        ticketApprovalModel.approver( userMapper.toModel( entity.getApprover() ) );
        ticketApprovalModel.action( entity.getAction() );
        ticketApprovalModel.comment( entity.getComment() );
        ticketApprovalModel.actionAt( entity.getActionAt() );

        return ticketApprovalModel.build();
    }

    @Override
    public TicketApproval toEntity(TicketApprovalModel model) {
        if ( model == null ) {
            return null;
        }

        TicketApproval ticketApproval = new TicketApproval();

        ticketApproval.setId( model.getApprovalId() );
        ticketApproval.setAction( model.getAction() );
        ticketApproval.setActionAt( model.getActionAt() );
        ticketApproval.setApprover( userMapper.toEntity( model.getApprover() ) );
        ticketApproval.setComment( model.getComment() );
        ticketApproval.setCreatedAt( model.getCreatedAt() );
        if ( model.getCreatedBy() != null ) {
            ticketApproval.setCreatedBy( Long.parseLong( model.getCreatedBy() ) );
        }
        ticketApproval.setStatus( model.getStatus() );
        ticketApproval.setTicket( ticketMapper.toEntity( model.getTicket() ) );
        ticketApproval.setUpdatedAt( model.getUpdatedAt() );
        if ( model.getUpdatedBy() != null ) {
            ticketApproval.setUpdatedBy( Long.parseLong( model.getUpdatedBy() ) );
        }
        ticketApproval.setVersion( model.getVersion() );

        return ticketApproval;
    }
}
