package com.fis.hrmservice.domain.model.ticket;

import com.fis.hrmservice.domain.model.contant.ApprovalTicketAction;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketApprovalModel {
    Long approvalId;
    Long ticketId;
    Long approverId;
    ApprovalTicketAction action;
    String comments;
}
