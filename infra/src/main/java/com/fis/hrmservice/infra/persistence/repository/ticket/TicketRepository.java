package com.fis.hrmservice.infra.persistence.repository.ticket;

import com.fis.hrmservice.infra.persistence.entity.Ticket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

  @Query("SELECT COUNT(t) FROM Ticket t WHERE t.ticketType.typeName = 'REGISTRATION'")
  int allRegistrationCount();

  @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'PENDING' AND t.ticketType.typeName = 'REGISTRATION'")
  int allPendingRegistrationCount();

  @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'REJECTED' AND t.ticketType.typeName = 'REGISTRATION'")
  int allRejectedRegistrationCount();

  @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'APPROVED' AND t.ticketType.typeName = 'REGISTRATION'")
  int allApprovedRegistrationCount();

  @Modifying
  @Query(
      """
        UPDATE Ticket t
        SET t.status = 'APPROVED'
        WHERE t.id IN :ticketIds
    """)
  int multipleApproval(@Param("ticketIds") List<Long> ticketIds);

  @Query("""
          SELECT t
          FROM Ticket t
          JOIN t.user u
          WHERE (:keyword IS NULL 
                 OR LOWER(u.companyEmail) LIKE LOWER(CONCAT('%', :keyword, '%'))
                 OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:status IS NULL OR t.status = :status)
            AND t.ticketType.typeName = 'REGISTRATION'
          """)
  List<Ticket> filterTickets(@Param("keyword") String keyword,
                             @Param("status") String status);

  @Query("SELECT t from Ticket t ORDER BY t.startAt DESC limit 3")
  List<Ticket> firstThreeRegistrationTicket();

  @Query("SELECT t from Ticket t WHERE t.id = :ticketId AND t.ticketType.typeName = 'REGISTRATION'")
  Ticket getDetailRegistrationTicket(Long ticketId);

  @Modifying
  @Transactional
  @Query("UPDATE Ticket t set t.status = :ticketStatus where t.id = :ticketId and t.ticketType.typeName = 'REGISTRATION'")
  int updateRegistrationTicketStatus(
          @Param("ticketStatus") String ticketStatus,
          @Param("ticketId") Long ticketId);
}
