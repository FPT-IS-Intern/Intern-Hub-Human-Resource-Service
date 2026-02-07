package com.fis.hrmservice.infra.persistence.adapter.ticket;

import com.fis.hrmservice.domain.model.ticket.TicketApprovalModel;
import com.fis.hrmservice.domain.port.output.ticket.TicketApprovalRepositoryPort;
import com.fis.hrmservice.infra.mapper.TicketApprovalMapper;
import com.fis.hrmservice.infra.persistence.entity.TicketApproval;
import com.fis.hrmservice.infra.persistence.repository.ticket.TicketApprovalJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TicketApprovalRepositoryAdapter implements TicketApprovalRepositoryPort {

    @Autowired
    private TicketApprovalJpaRepository ticketApprovalJpaRepository;

    @Autowired
    private TicketApprovalMapper ticketApprovalMapper;

    @Override
    public void save(TicketApprovalModel approvalModel) {
        TicketApproval entity = ticketApprovalMapper.toEntity(approvalModel);
        ticketApprovalJpaRepository.save(entity);
    }
}
