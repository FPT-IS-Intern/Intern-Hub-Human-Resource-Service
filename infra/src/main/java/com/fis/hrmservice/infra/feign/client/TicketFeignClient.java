package com.fis.hrmservice.infra.feign.client;

import com.fis.hrmservice.infra.configuration.FeignMultipartConfig;
import com.fis.hrmservice.infra.feign.response.TicketResponse;
import com.intern.hub.library.common.dto.ResponseApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "ticket", url = "${services.ticket.url}", configuration = FeignMultipartConfig.class)
public interface TicketFeignClient {

    @PostMapping(value = "/ticket/internal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseApi<TicketResponse> createTicketInternal(
            @RequestParam("creatorId") Long creatorId,
            @RequestPart("request") MultipartFile request,
            @RequestPart(value = "evidences", required = false) MultipartFile[] evidences);
}