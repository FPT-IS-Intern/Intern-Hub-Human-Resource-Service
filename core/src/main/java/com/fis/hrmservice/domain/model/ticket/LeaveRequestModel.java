package com.fis.hrmservice.domain.model.ticket;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LeaveRequestModel extends TicketModel{
    Long ticketId;
    Long leaveTypeId;
    int totalDays;
}