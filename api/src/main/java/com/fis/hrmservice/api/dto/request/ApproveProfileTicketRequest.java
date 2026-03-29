package com.fis.hrmservice.api.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record ApproveProfileTicketRequest(
        @NotNull(message = "ticketId is required")
        Long ticketId,

        @NotNull(message = "payload is required")
        Map<String, Object> payload) {
}
