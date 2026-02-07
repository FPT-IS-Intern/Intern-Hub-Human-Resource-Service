package com.fis.hrmservice.api.controller.ticket;

import com.fis.hrmservice.api.dto.request.CreateTicketRequest;
import com.fis.hrmservice.api.dto.request.LeaveTicketRequest;
import com.fis.hrmservice.api.dto.request.RemoteTicketRequest;
import com.fis.hrmservice.api.dto.response.CreateTicketResponse;
import com.fis.hrmservice.api.mapper.TicketApiMapper;
import com.fis.hrmservice.domain.model.ticket.LeaveRequestModel;
import com.fis.hrmservice.domain.usecase.command.ticket.CreateTicketCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.LeaveRequestCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.RemoteRequestCommand;
import com.fis.hrmservice.domain.usecase.implement.ticket.TicketUseCaseImpl;
import com.intern.hub.library.common.annotation.EnableGlobalExceptionHandler;
import com.intern.hub.library.common.dto.ResponseApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("hrm-serice/api/ticket")
@EnableGlobalExceptionHandler
@Slf4j
@Tag(name = "Ticket Management", description = "APIs for Ticket")
public class TicketController {

    @Autowired
    private TicketUseCaseImpl ticketUseCaseImpl;

    @Autowired
    private TicketApiMapper ticketApiMapper;

    @PostMapping(
            value = "/leave-ticket/{userId}",
            consumes = {
                    MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public ResponseApi<CreateTicketResponse> createLeaveTicket(
            @PathVariable Long userId,
            @RequestPart("ticketInfo") CreateTicketRequest ticketRequest,
            @RequestPart(value = "evidenceFile", required = false) MultipartFile evidenceFile
    ) {

        //chuyển request từ controller sang command ở dưới core
        CreateTicketCommand requestCommand = mapToTicketCommand(ticketRequest);
        //set file evidence
        requestCommand.setEvidence(evidenceFile);
        
        LeaveRequestCommand leaveCommand = LeaveRequestCommand.builder().build();

        LeaveRequestModel leaveModel = ticketUseCaseImpl.createLeaveRequest(requestCommand, leaveCommand, userId);

        return ResponseApi.ok(ticketApiMapper.toTicketResponse(leaveModel));
    }

    @PostMapping(value = "remote-ticket/{userid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseApi<?> createRemoteTicket(
            @PathVariable Long userid,
            @RequestPart("ticketInfo") CreateTicketRequest ticketRequest,
            @RequestPart("evidenceFile") MultipartFile evidenceFile,
            @RequestPart("remoteTicketInfo") RemoteTicketRequest remoteTicketRequest
            ) {
        CreateTicketCommand requestCommand = mapToTicketCommand(ticketRequest);
        requestCommand.setEvidence(evidenceFile);
        RemoteRequestCommand remoteCommand = ticketApiMapper.toRemoteRequestCommand(remoteTicketRequest);
        ticketUseCaseImpl.createRemoteRequest(requestCommand, remoteCommand, userid);

        return ResponseApi.ok(null);
    }

    private CreateTicketCommand mapToTicketCommand(CreateTicketRequest request) {
        return ticketApiMapper.toTicketCommand(request);
    }

}