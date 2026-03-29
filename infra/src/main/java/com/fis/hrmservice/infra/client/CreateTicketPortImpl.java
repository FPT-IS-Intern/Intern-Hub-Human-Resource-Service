package com.fis.hrmservice.infra.client;

import com.fis.hrmservice.domain.model.dto.request.CreateTicketInternalRequest;
import com.fis.hrmservice.domain.model.dto.resonse.CreateTicketInternalResponse;
import com.fis.hrmservice.domain.port.output.feign.CreateTicketPort;
import com.fis.hrmservice.infra.feign.client.TicketFeignClient;
import com.fis.hrmservice.infra.feign.request.CreateTicketRequest;
import com.fis.hrmservice.infra.feign.response.TicketResponse;
import com.intern.hub.library.common.dto.ResponseApi;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CreateTicketPortImpl implements CreateTicketPort {

    TicketFeignClient ticketFeignClient;
    ObjectMapper objectMapper;

    @Override
    public CreateTicketInternalResponse createTicket(Long userId, CreateTicketInternalRequest request, MultipartFile[] evidenceFiles) {

        try {
            CreateTicketRequest feignRequest = new CreateTicketRequest(
                    request.ticketTypeId(),
                    request.payload(),
                    request.evidences()
            );

            String requestJson = objectMapper.writeValueAsString(feignRequest); // 🔥

            ResponseApi<TicketResponse> response =
                    ticketFeignClient.createTicketInternal(userId, requestJson, evidenceFiles);

            TicketResponse ticketResponse = response.data();
            return new CreateTicketInternalResponse(ticketResponse.ticketId(), ticketResponse.status());

        } catch (Exception e) {
            throw new RuntimeException("Error serialize request", e);
        }
    }
}
