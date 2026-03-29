package com.fis.hrmservice.domain.model.dto.resonse;

import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

public record CreateTicketInternalResponse(
        @JsonSerialize(using = ToStringSerializer.class) Long ticketId,

        String status) {
}
