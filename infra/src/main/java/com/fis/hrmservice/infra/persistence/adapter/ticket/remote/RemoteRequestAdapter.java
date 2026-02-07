package com.fis.hrmservice.infra.persistence.adapter.ticket.remote;

import com.fis.hrmservice.domain.model.ticket.RemoteRequestModel;
import com.fis.hrmservice.domain.port.output.ticket.remoteticket.RemoteRequestRepositoryPort;
import com.fis.hrmservice.infra.mapper.RemoteRequestMapper;
import com.fis.hrmservice.infra.persistence.entity.RemoteRequest;
import com.fis.hrmservice.infra.persistence.entity.Ticket;
import com.fis.hrmservice.infra.persistence.repository.ticket.RemoteTicketRepository;
import com.fis.hrmservice.infra.persistence.repository.ticket.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RemoteRequestAdapter implements RemoteRequestRepositoryPort {

    @Autowired
    private RemoteRequestMapper remoteRequestMapper;

    @Autowired
    private RemoteTicketRepository remoteTicketRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    @Transactional
    public RemoteRequestModel save(RemoteRequestModel remoteRequest) {

        RemoteRequest request = remoteRequestMapper.toEntity(remoteRequest);

        if (request.getTickets() == null) {
            throw new IllegalStateException("RemoteRequest must reference a Ticket");
        }

        Ticket ticket = request.getTickets();

        if (ticket.getId() != null) {
            Ticket managed = ticketRepository.findById(ticket.getId())
                    .orElseThrow(() -> new IllegalStateException("Ticket not found: " + ticket.getId()));
            request.setTickets(managed);
            request.setId(managed.getId());
        } else {
            Ticket savedTicket = ticketRepository.saveAndFlush(ticket);
            request.setTickets(savedTicket);
            request.setId(savedTicket.getId());
        }

        return remoteRequestMapper.toModel(remoteTicketRepository.saveAndFlush(request));
    }
}
