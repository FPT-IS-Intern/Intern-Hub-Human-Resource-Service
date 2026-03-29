package com.fis.hrmservice.domain.port.output.feign;

import com.fis.hrmservice.domain.model.dto.request.CreateTicketInternalRequest;
import com.fis.hrmservice.domain.model.dto.resonse.CreateTicketInternalResponse;

import org.springframework.web.multipart.MultipartFile;

public interface CreateTicketPort {
    CreateTicketInternalResponse createTicket(Long userId, CreateTicketInternalRequest request, MultipartFile[] evidenceFiles);
}
