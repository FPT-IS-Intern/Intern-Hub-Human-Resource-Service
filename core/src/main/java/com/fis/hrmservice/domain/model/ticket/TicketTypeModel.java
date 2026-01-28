package com.fis.hrmservice.domain.model.ticket;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketTypeModel {
    Long ticketId;
    String typeName;
    String description;
}