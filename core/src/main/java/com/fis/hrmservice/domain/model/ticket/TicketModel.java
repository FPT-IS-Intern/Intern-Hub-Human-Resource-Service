package com.fis.hrmservice.domain.model.ticket;

import com.fis.hrmservice.domain.model.contant.TicketStatus;

import java.time.LocalDate;

public class TicketModel {
    Long ticketId;
    Long sender;
    Long tickerTypeId;
    LocalDate startDate;
    LocalDate endDate;
    String reason;
    TicketStatus ticketStatus;
}
