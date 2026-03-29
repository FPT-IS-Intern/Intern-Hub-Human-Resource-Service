package com.fis.hrmservice.infra.client;

import com.fis.hrmservice.domain.model.dto.request.CreateTicketInternalRequest;
import com.fis.hrmservice.domain.model.dto.resonse.CreateTicketInternalResponse;
import com.fis.hrmservice.domain.port.output.feign.CreateTicketPort;
import com.fis.hrmservice.infra.feign.client.TicketFeignClient;
import com.fis.hrmservice.infra.feign.response.TicketResponse;
import com.intern.hub.library.common.dto.ResponseApi;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CreateTicketPortImpl implements CreateTicketPort {

    TicketFeignClient ticketFeignClient;
    ObjectMapper objectMapper;

    @Override
    public CreateTicketInternalResponse createTicket(Long userId, CreateTicketInternalRequest request, MultipartFile[] evidenceFiles) {

        try {
            // Only serialize ticketTypeId and payload — MultipartFile[] cannot be serialized by Jackson
            Map<String, Object> jsonBody = new LinkedHashMap<>();
            jsonBody.put("ticketTypeId", request.ticketTypeId());
            jsonBody.put("payload", request.payload());

            String requestJson = objectMapper.writeValueAsString(jsonBody);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestPart = new HttpEntity<>(requestJson, headers);

            ResponseApi<TicketResponse> response =
                    ticketFeignClient.createTicketInternal(userId, requestPart, evidenceFiles);

            TicketResponse ticketResponse = response.data();
            return new CreateTicketInternalResponse(ticketResponse.ticketId(), ticketResponse.status());

        } catch (Exception e) {
            throw new RuntimeException("Error serialize request", e);
        }
    }
}
