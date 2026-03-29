package com.fis.hrmservice.infra.client;

import com.fis.hrmservice.domain.model.dto.request.CreateTicketInternalRequest;
import com.fis.hrmservice.domain.model.dto.resonse.CreateTicketInternalResponse;
import com.fis.hrmservice.domain.port.output.feign.CreateTicketPort;
import com.fis.hrmservice.infra.feign.client.TicketFeignClient;
import com.fis.hrmservice.infra.feign.response.TicketResponse;
import com.intern.hub.library.common.dto.ResponseApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateTicketPortImpl implements CreateTicketPort {

    private final TicketFeignClient ticketFeignClient;

    @Override
    public CreateTicketInternalResponse createTicket(
            Long userId,
            CreateTicketInternalRequest request,
            MultipartFile[] evidenceFiles
    ) {
        try {
            ResponseApi<TicketResponse> response =
                    ticketFeignClient.createTicketInternal(userId, request, evidenceFiles);

            if (response == null || response.data() == null) {
                throw new RuntimeException("Ticket service returned empty response");
            }

            TicketResponse ticketResponse = response.data();

            return new CreateTicketInternalResponse(
                    ticketResponse.ticketId(),
                    ticketResponse.status()
            );

        } catch (Exception e) {
            log.error("[CreateTicketPort] Failed to create ticket. userId={}", userId, e);
            throw new RuntimeException("Error calling Ticket Service", e);
        }
    }
}