package com.fis.hrmservice.domain.usecase.implement.ticket;

import com.fis.hrmservice.domain.model.constant.TicketStatus;
import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.ticket.*;
import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.feign.CreateAuthIdentityPort;
import com.fis.hrmservice.domain.port.output.ticket.TicketRepositoryPort;
import com.fis.hrmservice.domain.port.output.ticket.TicketTypeRepositoryPort;
import com.fis.hrmservice.domain.port.output.user.PositionRepositoryPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.ticket.CreateTicketCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.FilterRegistrationTicketCommand;
import com.fis.hrmservice.domain.utils.helper.DateValidationHelper;
import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.library.common.utils.Snowflake;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketUseCaseImpl {

  TicketRepositoryPort ticketRepositoryPort;

  TicketTypeRepositoryPort ticketTypeRepositoryPort;

  UserRepositoryPort userRepositoryPort;

  PositionRepositoryPort positionRepositoryPort;

  CreateAuthIdentityPort createAuthIdentityPort;

  Snowflake snowflake;

  /* ================= BASE TICKET ================= */

  private TicketModel createBaseTicket(CreateTicketCommand command, Long userId) {

    UserModel requester =
        userRepositoryPort
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found: " + userId));

    DateValidationHelper.validateDate(command.getFromDate(), command.getToDate());

    TicketTypeModel ticketTypeModel =
        ticketTypeRepositoryPort.findTicketTypeByCode(command.getTicketType());

    if (ticketTypeModel == null) {
      throw new NotFoundException("Ticket type not found: " + command.getTicketType());
    }

    TicketModel ticket =
        TicketModel.builder()
            .ticketId(snowflake.next())
            .requester(requester)
            .ticketType(ticketTypeModel)
            .startAt(command.getFromDate())
            .endAt(command.getToDate())
            .reason(command.getReason())
            .sysStatus(TicketStatus.PENDING)
            .build();

    return ticketRepositoryPort.save(ticket);
  }

  public PaginatedData<TicketModel> listRegistrationTicketPaged(
      FilterRegistrationTicketCommand command, int page, int size) {
    return ticketRepositoryPort.filterRegistrationTicketPaged(command, page, size);
  }

  public List<TicketModel> firstThreeRegistrationTicket() {
    return ticketRepositoryPort.firstThreeRegistrationTicket();
  }

  public TicketModel getDetailRegistrationTicket(Long ticketId) {
    log.info("=== Getting detail for ticketId: {} ===", ticketId);

    TicketModel ticket = ticketRepositoryPort.getDetailRegistrationTicket(ticketId);

    log.info("Query result: ticket={}", ticket);

    if (ticket == null) {
      log.warn("Ticket not found with id: {} and type REGISTRATION", ticketId);
      throw new NotFoundException("Ticket not found: " + ticketId);
    }

    log.info(
        "Ticket found: id={}, requester={}, type={}",
        ticket.getTicketId(),
        ticket.getRequester() != null ? ticket.getRequester().getFullName() : "null",
        ticket.getTicketType() != null ? ticket.getTicketType().getTypeName() : "null");

    return ticket;
  }

  @Transactional
  public TicketModel approveRegistrationTicketByTicketId(Long ticketId) {
    TicketModel ticket = ticketRepositoryPort.findById(ticketId);

    if (ticket == null) {
      throw new NotFoundException("Ticket not found: " + ticketId);
    }

    if (ticket.getSysStatus() != TicketStatus.PENDING) {
      throw new ConflictDataException("Only pending registration ticket can be approved");
    }

    Map<String, Object> userInfoTemp = ticket.getUserInfoTemp();
    if (userInfoTemp == null || userInfoTemp.isEmpty()) {
      throw new ConflictDataException("Missing user_profile_temp in registration ticket: " + ticketId);
    }

    UserModel userToCreate = buildApprovedUserFromTemp(userInfoTemp);

    if (userRepositoryPort.existsByEmail(userToCreate.getCompanyEmail())) {
      throw new ConflictDataException("Tài khoản đã được đăng ký");
    }

    if (userRepositoryPort.existsByIdNumber(userToCreate.getIdNumber())) {
      throw new ConflictDataException("Số CCCD đã được dùng để đăng ký");
    }

    UserModel savedUser = userRepositoryPort.create(userToCreate);

    ticket.setRequester(savedUser);
    ticket.setSysStatus(TicketStatus.APPROVED);
    TicketModel approvedTicket = ticketRepositoryPort.save(ticket);

    Long userId = savedUser.getUserId();
    String email = savedUser.getCompanyEmail();

    log.info("Approving registration ticket {} for userId {}", ticketId, userId);

    TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
              @Override
              public void afterCommit() {
                try {
                  createAuthIdentityPort.createAuthIdentity(userId, email);
                  log.info("Auth identity created for userId {}", userId);

                } catch (Exception e) {
                  log.error("Failed to sync user {} to auth service", userId, e);
                }
              }
            }
    );

    return approvedTicket;
  }

  private UserModel buildApprovedUserFromTemp(Map<String, Object> userInfoTemp) {
    String positionCode = readString(userInfoTemp, "positionCode");

    PositionModel position =
        positionRepositoryPort
            .findByCode(positionCode)
            .orElseThrow(() -> new ConflictDataException("Position không tồn tại"));

    return UserModel.builder()
        .userId(readLong(userInfoTemp, "userId", snowflake.next()))
        .position(position)
        .fullName(readString(userInfoTemp, "fullName"))
        .idNumber(readString(userInfoTemp, "idNumber"))
        .dateOfBirth(readLocalDate(userInfoTemp, "dateOfBirth"))
        .companyEmail(readString(userInfoTemp, "companyEmail").toLowerCase())
        .phoneNumber(readString(userInfoTemp, "phoneNumber"))
        .address(readString(userInfoTemp, "address"))
        .internshipStartDate(readLocalDate(userInfoTemp, "internshipStartDate"))
        .internshipEndDate(readLocalDate(userInfoTemp, "internshipEndDate"))
        .avatarUrl(readString(userInfoTemp, "avatarUrl"))
        .cvUrl(readString(userInfoTemp, "cvUrl"))
        .sysStatus(UserStatus.APPROVED)
        .build();
  }

  private String readString(Map<String, Object> source, String key) {
    Object value = source.get(key);
    if (value == null) {
      throw new ConflictDataException("Missing required field in user_profile_temp: " + key);
    }
    return String.valueOf(value);
  }

  private Long readLong(Map<String, Object> source, String key, Long defaultValue) {
    Object value = source.get(key);
    if (value == null) {
      return defaultValue;
    }
    if (value instanceof Number number) {
      return number.longValue();
    }
    try {
      return Long.parseLong(String.valueOf(value));
    } catch (NumberFormatException ex) {
      throw new ConflictDataException("Invalid numeric field in user_profile_temp: " + key);
    }
  }

  private LocalDate readLocalDate(Map<String, Object> source, String key) {
    Object value = source.get(key);
    if (value == null) {
      return null;
    }
    return LocalDate.parse(String.valueOf(value));
  }

  public TicketModel rejectRegistrationTicketByTicketId(Long ticketId) {
    return ticketRepositoryPort.updateRegistrationTicketStatus(ticketId, TicketStatus.REJECTED);
  }

  public TicketModel suspendRegistrationTicketByTicketId(Long ticketId) {
    return ticketRepositoryPort.updateRegistrationTicketStatus(ticketId, TicketStatus.SUSPENDED);
  }

  public int allRegistrationTicket() {
    return ticketRepositoryPort.getAllRegistrationTicket();
  }

  public int allApprovedRegistrationTicket() {
    return ticketRepositoryPort.getAllRegistrationTicketApproved();
  }

  public int allRejectedRegistrationTicket() {
    return ticketRepositoryPort.getAllRegistrationTicketRejected();
  }

  public int allPendingRegistrationTicket() {
    return ticketRepositoryPort.getAllRegistrationTicketPending();
  }
}
