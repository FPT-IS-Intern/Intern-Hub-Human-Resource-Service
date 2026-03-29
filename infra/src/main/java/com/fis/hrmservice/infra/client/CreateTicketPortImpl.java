package com.fis.hrmservice.infra.client;

import tools.jackson.databind.ObjectMapper;
import com.fis.hrmservice.domain.model.dto.request.CreateTicketInternalRequest;
import com.fis.hrmservice.domain.model.dto.resonse.CreateTicketInternalResponse;
import com.fis.hrmservice.domain.port.output.feign.CreateTicketPort;
import com.fis.hrmservice.infra.feign.client.TicketFeignClient;
import com.fis.hrmservice.infra.feign.response.TicketResponse;
import com.intern.hub.library.common.dto.ResponseApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateTicketPortImpl implements CreateTicketPort {

    private final TicketFeignClient ticketFeignClient;
    private final ObjectMapper objectMapper;

    @Override
    public CreateTicketInternalResponse createTicket(Long userId, CreateTicketInternalRequest request, MultipartFile[] evidenceFiles) {
        try {
            // Serialize ticketTypeId + payload into JSON bytes.
            // MultipartFile[] cannot be serialized — it goes through as a separate multipart part.
            MultiValueMap<String, Object> jsonPart = new LinkedMultiValueMap<>();
            jsonPart.add("ticketTypeId", request.ticketTypeId());
            jsonPart.add("payload", request.payload());

            byte[] jsonBytes = objectMapper.writeValueAsBytes(jsonPart);

            // Build headers for the "request" multipart part. SpringEncoder checks for
            // HttpEntity values in a MultiValueMap and uses the entity's headers as the
            // part's headers (including Content-Type: application/json). Content-Disposition
            // is needed so @RequestPart("request") on the receiving side resolves correctly.
            HttpHeaders partHeaders = new HttpHeaders();
            partHeaders.setContentType(MediaType.APPLICATION_JSON);
            partHeaders.set("Content-Disposition", "form-data; name=\"request\"; filename=\"request\"");

            HttpEntity<ByteArrayResource> partEntity = new HttpEntity<>(
                    new ByteArrayResource(jsonBytes) {
                        @Override
                        public String getFilename() {
                            return "request";
                        }
                    },
                    partHeaders
            );

            // Build the multipart body for the Feign call.
            LinkedMultiValueMap<String, Object> requestParts = new LinkedMultiValueMap<>();
            requestParts.add("request", partEntity);

            log.debug("[CreateTicketPort] Sending multipart ticket request. ticketTypeId={}, payloadKeys={}",
                    request.ticketTypeId(),
                    request.payload() != null ? request.payload().keySet() : "null");

            ResponseApi<TicketResponse> response =
                    ticketFeignClient.createTicketInternal(userId, requestParts, null);

            TicketResponse ticketResponse = response.data();
            return new CreateTicketInternalResponse(ticketResponse.ticketId(), ticketResponse.status());

        } catch (Exception e) {
            log.error("[CreateTicketPort] Failed to create ticket. userId={}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Error creating ticket", e);
        }
    }
}
