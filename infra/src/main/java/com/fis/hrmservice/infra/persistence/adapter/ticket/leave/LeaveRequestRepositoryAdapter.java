
package com.fis.hrmservice.infra.persistence.adapter.ticket.leave;

import com.fis.hrmservice.domain.model.ticket.LeaveRequestModel;
import com.fis.hrmservice.domain.port.output.ticket.leaveticket.LeaveRequestRepositoryPort;
import com.fis.hrmservice.infra.mapper.LeaveRequestMapper;
import com.fis.hrmservice.infra.persistence.entity.LeaveRequest;
import com.fis.hrmservice.infra.persistence.entity.Ticket;
import com.fis.hrmservice.infra.persistence.repository.ticket.LeaveTicketRepository;
import com.fis.hrmservice.infra.persistence.repository.ticket.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class LeaveRequestRepositoryAdapter implements LeaveRequestRepositoryPort {

    @Autowired
    private LeaveTicketRepository leaveTicketRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private LeaveRequestMapper leaveRequestMapper;

    @Override
    @Transactional
    public LeaveRequestModel save(LeaveRequestModel model) {

        // Map domain → entity
        LeaveRequest leave = leaveRequestMapper.toEntity(model);

        // Ensure Ticket is managed and persisted before saving LeaveRequest.
        // Many schemas use ticket_id as the PK for leave_requests (via @MapsId),
        // so Ticket must have a non-null identifier.
        if (leave.getTicket() == null) {
            throw new IllegalStateException("LeaveRequest must reference a Ticket");
        }

        Ticket ticket = leave.getTicket();

        if (ticket.getId() != null) {
            // Attach existing ticket as managed entity
            Ticket managed = ticketRepository.findById(ticket.getId())
                    .orElseThrow(() -> new IllegalStateException("Ticket not found: " + ticket.getId()));
            leave.setTicket(managed);
            leave.setId(managed.getId()); // Synchronize ID for @MapsId
        } else {
            // Persist new ticket first to obtain its id (avoids null identifier on LeaveRequest)
            // Using saveAndFlush to ensure the ID is generated and available in the session
            Ticket savedTicket = ticketRepository.saveAndFlush(ticket);
            leave.setTicket(savedTicket);
            leave.setId(savedTicket.getId()); // Synchronize ID for @MapsId
        }

        // Save LeaveRequest. Hibernate will use the synchronized ID.
        LeaveRequest saved = leaveTicketRepository.saveAndFlush(leave);

        return leaveRequestMapper.toModel(saved);
    }
}
