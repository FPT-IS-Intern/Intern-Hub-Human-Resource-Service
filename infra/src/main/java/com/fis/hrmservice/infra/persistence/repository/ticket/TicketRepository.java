package com.fis.hrmservice.infra.persistence.repository.ticket;

import com.fis.hrmservice.domain.model.constant.TicketStatus;
import com.fis.hrmservice.domain.usecase.command.ticket.FilterRegistrationTicketCommand;
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

  @Query(
      "SELECT COUNT(t) FROM Ticket t WHERE t.status = 'PENDING' AND t.ticketType.typeName = 'REGISTRATION'")
  int allPendingRegistrationCount();

  @Query(
      "SELECT COUNT(t) FROM Ticket t WHERE t.status = 'REJECTED' AND t.ticketType.typeName = 'REGISTRATION'")
  int allRejectedRegistrationCount();

  @Query(
      "SELECT COUNT(t) FROM Ticket t WHERE t.status = 'APPROVED' AND t.ticketType.typeName = 'REGISTRATION'")
  int allApprovedRegistrationCount();

  @Modifying
  @Query(
      """
                        UPDATE Ticket t
                        SET t.status = 'APPROVED'
                        WHERE t.id IN :ticketIds
                    """)
  int multipleApproval(@Param("ticketIds") List<Long> ticketIds);

  @Query(
      """
                        SELECT t
                        FROM Ticket t
                        JOIN t.user u
                        JOIN t.ticketType tt
                        WHERE (
                                :keyword IS NULL
                                OR LOWER(TRIM(u.companyEmail)) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%'))
                                OR LOWER(TRIM(u.fullName)) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%'))
                              )
                          AND (:status IS NULL OR t.status = :status)
                          AND tt.typeName = 'REGISTRATION'
                    """)
  List<Ticket> filterTickets(@Param("keyword") String keyword, @Param("status") String status);

  @Query(
      """
                        SELECT t
                        FROM Ticket t
                        JOIN t.user u
                        JOIN t.ticketType tt
                        WHERE (
                                :#{#command.keyword} IS NULL OR :#{#command.keyword} = ''
                                OR LOWER(TRIM(u.companyEmail)) LIKE LOWER(CONCAT('%', TRIM(:#{#command.keyword}), '%'))
                                OR LOWER(TRIM(u.fullName)) LIKE LOWER(CONCAT('%', TRIM(:#{#command.keyword}), '%'))
                              )
                          AND (:#{#command.ticketStatus} IS NULL OR t.status = :#{#command.ticketStatus})

                          AND tt.typeName = 'REGISTRATION'
                        ORDER BY t.startAt DESC
                    """)
  Page<Ticket> filterRegistrationTicketPaged(
      @Param("command") FilterRegistrationTicketCommand command, Pageable pageable);

  @Query(
      "SELECT t from Ticket t WHERE t.ticketType.typeName = 'REGISTRATION' ORDER BY t.startAt DESC limit 3")
  List<Ticket> firstThreeRegistrationTicket();

  @Query(
      """
                        SELECT t
                        FROM Ticket t
                        JOIN FETCH t.ticketType tt
                        JOIN FETCH t.user u
                        LEFT JOIN FETCH u.avatar
                        LEFT JOIN FETCH u.cv
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
      """
      SELECT COUNT(t) > 0 FROM Ticket t
      WHERE t.user.userId = :userId
      AND :date BETWEEN t.startAt AND t.endAt
      AND t.status = 'APPROVED'
      """)
  boolean existsApprovedTicketByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}
