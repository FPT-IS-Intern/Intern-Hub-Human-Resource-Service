package com.fis.hrmservice.domain.port.output.ticket.leaveticket;

import com.fis.hrmservice.domain.model.ticket.LeaveRequestModel;

public interface LeaveRequestRepositoryPort {
  LeaveRequestModel save(LeaveRequestModel user);
}
