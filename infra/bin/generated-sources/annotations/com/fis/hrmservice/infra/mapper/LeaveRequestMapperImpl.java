package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.ticket.LeaveRequestModel;
import com.fis.hrmservice.infra.persistence.entity.LeaveRequest;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-10T10:06:37+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class LeaveRequestMapperImpl implements LeaveRequestMapper {

    @Autowired
    private TicketMapper ticketMapper;

    @Override
    public LeaveRequestModel toModel(LeaveRequest entity) {
        if ( entity == null ) {
            return null;
        }

        LeaveRequestModel.LeaveRequestModelBuilder leaveRequestModel = LeaveRequestModel.builder();

        leaveRequestModel.ticket( ticketMapper.toModel( entity.getTicket() ) );
        leaveRequestModel.totalDays( entity.getTotalDays() );

        return leaveRequestModel.build();
    }

    @Override
    public LeaveRequest toEntity(LeaveRequestModel model) {
        if ( model == null ) {
            return null;
        }

        LeaveRequest leaveRequest = new LeaveRequest();

        leaveRequest.setTicket( ticketMapper.toEntity( model.getTicket() ) );
        leaveRequest.setTotalDays( model.getTotalDays() );

        return leaveRequest;
    }
}
