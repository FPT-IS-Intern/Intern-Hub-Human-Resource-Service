package com.fis.hrmservice.infra.persistence.repository.ticket;

import com.fis.hrmservice.infra.persistence.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveTicketRepository extends JpaRepository<LeaveRequest, Long> {}
