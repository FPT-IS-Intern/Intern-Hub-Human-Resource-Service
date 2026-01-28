package com.fis.hrmservice.domain.model.ticket;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RemoteRequestModel extends TicketModel{
    Long ticketId;
    Long workLocationId;
    LocalDate startTime;
    LocalDate endTime;
}