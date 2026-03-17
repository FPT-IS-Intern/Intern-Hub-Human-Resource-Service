package com.fis.hrmservice.infra.persistence.repository.ticket;

import com.fis.hrmservice.domain.model.constant.TicketStatus;
import com.fis.hrmservice.infra.persistence.entity.Ticket;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

  @Query("SELECT COUNT(t) FROM Ticket t WHERE t.ticketType.typeName = 'REGISTRATION'")
  int allRegistrationCount();

  @Query(
      "SELECT COUNT(t) FROM Ticket t WHERE t.status = 'PENDING' AND t.ticketType.typeName = 'REGISTRATION'")
  int allPendingRegistrationCount();

  @Query(
      "SELECT COUNT(t) FROM Ticket t WHERE t.status = 'REJECTED' AND t.ticketType.typeName = 'REGISTRATION'")
  int allRejectedRegistrationCount();

  @Query(
      "SELECT COUNT(t) FROM Ticket t WHERE t.status = 'APPROVED' AND t.ticketType.typeName = 'REGISTRATION'")
  int allApprovedRegistrationCount();

  @Query(
      value = """
          SELECT t.*
          FROM tickets t
          JOIN ticket_types tt ON tt.ticket_type_id = t.ticket_type_id
          WHERE tt.type_name = 'REGISTRATION'
            AND (
              CAST(:keyword AS text) IS NULL
              OR CAST(:keyword AS text) = ''
              OR LOWER(COALESCE(t.user_info_temp, '{}'::jsonb) ->> 'companyEmail') LIKE LOWER('%' || CAST(:keyword AS text) || '%')
              OR LOWER(COALESCE(t.user_info_temp, '{}'::jsonb) ->> 'fullName') LIKE LOWER('%' || CAST(:keyword AS text) || '%')
            )
            AND (
              CAST(:status AS text) IS NULL
              OR CAST(:status AS text) = ''
              OR t.status = CAST(:status AS text)
            )
          ORDER BY t.start_at DESC
          """,
      nativeQuery = true)
  List<Ticket> filterTickets(@Param("keyword") String keyword, @Param("status") String status);

  @Query(
      value = """
          SELECT t.*
          FROM tickets t
          JOIN ticket_types tt ON tt.ticket_type_id = t.ticket_type_id
          WHERE tt.type_name = 'REGISTRATION'
            AND (
              CAST(:keyword AS text) IS NULL
              OR CAST(:keyword AS text) = ''
              OR LOWER(COALESCE(t.user_info_temp, '{}'::jsonb) ->> 'companyEmail') LIKE LOWER('%' || CAST(:keyword AS text) || '%')
              OR LOWER(COALESCE(t.user_info_temp, '{}'::jsonb) ->> 'fullName') LIKE LOWER('%' || CAST(:keyword AS text) || '%')
            )
            AND (
              CAST(:ticketStatus AS text) IS NULL
              OR CAST(:ticketStatus AS text) = ''
              OR t.status = CAST(:ticketStatus AS text)
            )
          ORDER BY t.start_at DESC
          """,
      countQuery = """
          SELECT COUNT(*)
          FROM tickets t
          JOIN ticket_types tt ON tt.ticket_type_id = t.ticket_type_id
          WHERE tt.type_name = 'REGISTRATION'
            AND (
              CAST(:keyword AS text) IS NULL
              OR CAST(:keyword AS text) = ''
              OR LOWER(COALESCE(t.user_info_temp, '{}'::jsonb) ->> 'companyEmail') LIKE LOWER('%' || CAST(:keyword AS text) || '%')
              OR LOWER(COALESCE(t.user_info_temp, '{}'::jsonb) ->> 'fullName') LIKE LOWER('%' || CAST(:keyword AS text) || '%')
            )
            AND (
              CAST(:ticketStatus AS text) IS NULL
              OR CAST(:ticketStatus AS text) = ''
              OR t.status = CAST(:ticketStatus AS text)
            )
          """,
      nativeQuery = true)
  Page<Ticket> filterRegistrationTicketPaged(
      @Param("keyword") String keyword,
      @Param("ticketStatus") String ticketStatus,
      Pageable pageable);

  @Query(
      "SELECT t from Ticket t WHERE t.ticketType.typeName = 'REGISTRATION' ORDER BY t.startAt DESC limit 3")
  List<Ticket> firstThreeRegistrationTicket();

  @Query(
      """
                        SELECT t
                        FROM Ticket t
                        JOIN FETCH t.ticketType tt
                        WHERE t.id = :ticketId
                          AND tt.typeName = 'REGISTRATION'
                    """)
  Ticket getDetailRegistrationTicket(@Param("ticketId") Long ticketId);

  @Modifying
  @Transactional
  @Query(
      "UPDATE Ticket t set t.status = :ticketStatus where t.id = :ticketId and t.ticketType.typeName = 'REGISTRATION'")
  int updateRegistrationTicketStatus(
      @Param("ticketStatus") TicketStatus ticketStatus, @Param("ticketId") Long ticketId);

  @Query(
      value = """
      SELECT COUNT(t) > 0 FROM tickets t
      WHERE (t.user_info_temp ->> 'userId')::bigint = :userId
      AND :date BETWEEN t.start_at AND t.end_at
      AND t.status = 'APPROVED'
      """, nativeQuery = true)
  boolean existsApprovedTicketByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}
