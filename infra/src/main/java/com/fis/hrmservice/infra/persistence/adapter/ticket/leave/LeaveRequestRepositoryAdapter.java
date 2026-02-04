package com.fis.hrmservice.infra.persistence.adapter.ticket.leave;

import com.fis.hrmservice.domain.model.ticket.LeaveRequestModel;
import com.fis.hrmservice.domain.port.output.ticket.leaveticket.LeaveRequestRepositoryPort;
import com.fis.hrmservice.infra.mapper.LeaveRequestMapper;
import com.fis.hrmservice.infra.persistence.repository.ticket.LeaveTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LeaveRequestRepositoryAdapter implements LeaveRequestRepositoryPort {

  @Autowired private LeaveTicketRepository leaveTicketRepository;

  @Autowired private LeaveRequestMapper leaveRequestMapper;

  @Override
  public LeaveRequestModel save(LeaveRequestModel model) {
    return null;
  }
}
