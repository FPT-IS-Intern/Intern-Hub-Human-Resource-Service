package com.fis.hrmservice.infra.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fis.hrmservice.domain.model.dto.request.CreateTicketInternalRequest;
import com.fis.hrmservice.domain.model.dto.resonse.CreateTicketInternalResponse;
import com.fis.hrmservice.domain.port.output.feign.CreateTicketPort;
import com.fis.hrmservice.infra.feign.client.TicketFeignClient;
import com.fis.hrmservice.infra.feign.response.TicketResponse;
import com.fis.hrmservice.infra.feign.util.JsonMultipartFile;
import com.intern.hub.library.common.dto.ResponseApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateTicketPortImpl implements CreateTicketPort {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final TicketFeignClient ticketFeignClient;

    @Override
    public CreateTicketInternalResponse createTicket(
            Long userId,
            CreateTicketInternalRequest request,
            MultipartFile[] evidenceFiles
    ) {
        try {
            // Convert CreateTicketInternalRequest to JSON bytes and wrap as MultipartFile
            // This is necessary because SpringFormEncoder cannot serialize POJOs
            // into named JSON multipart parts correctly
            Map<String, Object> requestMap = new LinkedHashMap<>();
            requestMap.put("ticketTypeId", request.ticketTypeId());
            requestMap.put("payload", request.payload());

            byte[] jsonBytes = objectMapper.writeValueAsBytes(requestMap);
            MultipartFile requestPart = new JsonMultipartFile("request", jsonBytes);

            ResponseApi<TicketResponse> response =
                    ticketFeignClient.createTicketInternal(userId, requestPart, evidenceFiles);

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