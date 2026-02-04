package com.fis.hrmservice.infra.persistence.repository.ticket;

import com.fis.hrmservice.infra.persistence.entity.Ticket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

  @Query(
      """
                SELECT t FROM Ticket t
                WHERE
                    (
                        :nameOrEmail IS NULL
                        OR LOWER(t.user.fullName) LIKE LOWER(CONCAT('%', :nameOrEmail, '%'))
                        OR LOWER(t.user.companyEmail) LIKE LOWER(CONCAT('%', :nameOrEmail, '%'))
                    )
                AND
                    (
                        :ticketType IS NULL
                        OR t.ticketType.typeName = :ticketType
                    )
                AND
                    (
                        :ticketStatus IS NULL
                        OR t.status = :ticketStatus
                    )
            """)
  List<Ticket> filterTicket(
      @Param("nameOrEmail") String nameOrEmail,
      @Param("ticketType") String ticketType,
      @Param("ticketStatus") String ticketStatus);

  @Query("SELECT COUNT(t) FROM Ticket t")
  int allRequestsCount();

  @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'PENDING'")
  int allPendingRequestsCount();

  @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'WAITING'")
  int allWaitingRequestsCount();

  @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'APPROVED'")
  int allApprovedRequestsCount();

  @Modifying
  @Query(
      """
        UPDATE Ticket t
        SET t.status = 'APPROVED'
        WHERE t.id IN :ticketIds
    """)
  int multipleApproval(@Param("ticketIds") List<Long> ticketIds);
}
