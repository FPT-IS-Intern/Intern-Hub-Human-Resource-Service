package com.fis.hrmservice.infra.persistence.repository.ticket;

import com.fis.hrmservice.infra.persistence.entity.RemoteRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RemoteTicketRepository extends JpaRepository<RemoteRequest, Long> {}
