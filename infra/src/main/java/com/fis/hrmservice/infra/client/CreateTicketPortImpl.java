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

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CreateTicketPortImpl implements CreateTicketPort {

    TicketFeignClient ticketFeignClient;

    @Override
    public CreateTicketInternalResponse createTicket(Long userId, CreateTicketInternalRequest request, MultipartFile[] evidenceFiles) {
        CreateTicketRequest feignRequest = new CreateTicketRequest(
                request.ticketTypeId(),
                request.payload(),
                request.evidences());
        ResponseApi<TicketResponse> response = ticketFeignClient.createTicketInternal(userId, feignRequest, evidenceFiles);
        TicketResponse ticketResponse = response.data();
        return new CreateTicketInternalResponse(ticketResponse.ticketId(), ticketResponse.status());
    }
}
