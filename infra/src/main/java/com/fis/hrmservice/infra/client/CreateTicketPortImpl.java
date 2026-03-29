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

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateTicketPortImpl implements CreateTicketPort {

    private final TicketFeignClient ticketFeignClient;
    private final ObjectMapper objectMapper;

    @Override
    public CreateTicketInternalResponse createTicket(Long userId,
                                                     CreateTicketInternalRequest request,
                                                     MultipartFile[] evidenceFiles) {
        try {

            byte[] jsonBytes = objectMapper.writeValueAsBytes(request);

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

            ResponseApi<TicketResponse> response =
                    ticketFeignClient.createTicketInternal(userId, partEntity, evidenceFiles);

            TicketResponse ticketResponse = response.data();
            return new CreateTicketInternalResponse(
                    ticketResponse.ticketId(),
                    ticketResponse.status()
            );

        } catch (Exception e) {
            log.error("[CreateTicketPort] Failed to create ticket. userId={}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Error creating ticket", e);
        }
    }
}
