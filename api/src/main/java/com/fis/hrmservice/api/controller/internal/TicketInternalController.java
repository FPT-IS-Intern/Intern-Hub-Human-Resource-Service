package com.fis.hrmservice.api.controller.internal;

import com.fis.hrmservice.api.dto.request.ApproveProfileTicketRequest;
import com.fis.hrmservice.domain.service.ProfileUpdateApprovalService;
import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.Internal;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/hrm/internal/tickets")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TicketInternalController {

    ProfileUpdateApprovalService profileUpdateApprovalService;

    @PostMapping("/profile-approved")
    @Internal
    public ResponseApi<Void> onProfileTicketApproved(@Valid @RequestBody ApproveProfileTicketRequest request) {
        log.info("Received profile ticket approved callback. TicketId: {}", request.ticketId());
        profileUpdateApprovalService.applyApprovedProfile(request.ticketId(), request.payload());
        return ResponseApi.ok(null);
    }
}
