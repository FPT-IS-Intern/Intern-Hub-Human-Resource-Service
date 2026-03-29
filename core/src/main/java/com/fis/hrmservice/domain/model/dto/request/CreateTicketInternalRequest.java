package com.fis.hrmservice.domain.model.dto.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public record CreateTicketInternalRequest(
        @NotNull(message = "ticketTypeId is required")
        Long ticketTypeId,

        @NotNull(message = "payload is required")
        Map<String, Object> payload,

        MultipartFile[] evidences) {
}
