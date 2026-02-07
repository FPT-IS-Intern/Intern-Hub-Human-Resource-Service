package com.fis.hrmservice.domain.port.output.ticket;

import com.fis.hrmservice.domain.model.ticket.TicketApprovalModel;

public interface TicketApprovalRepositoryPort {
    void save(TicketApprovalModel approvalModel);
}
