package com.fis.hrmservice.domain.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Builder
public record CreateTicketInternalRequest(
        @NotNull(message = "ticketTypeId is required")
        Long ticketTypeId,

        @NotNull(message = "payload is required")
        Map<String, Object> payload,

        MultipartFile[] evidences) {
}
