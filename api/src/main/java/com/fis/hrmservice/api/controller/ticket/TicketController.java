package com.fis.hrmservice.api.controller.ticket;

import com.fis.hrmservice.api.dto.request.CreateTicketRequest;
import com.fis.hrmservice.api.dto.request.FilterRegistrationRequest;
import com.fis.hrmservice.api.dto.request.RemoteTicketRequest;
import com.fis.hrmservice.api.dto.response.FirstThreeRegistrationResponse;
import com.fis.hrmservice.api.dto.response.ListRegistrationResponse;
import com.fis.hrmservice.api.dto.response.RegistrationDetailResponse;
import com.fis.hrmservice.api.dto.response.TicketResponse;
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

import java.util.List;

@RestController
@RequestMapping("hrm-service/ticket")
@EnableGlobalExceptionHandler
@Slf4j
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4207"})
@Tag(name = "Ticket Management", description = "APIs for Ticket")
public class TicketController {

    @Autowired
    private TicketUseCaseImpl ticketUseCaseImpl;

    @Autowired
    private TicketApiMapper ticketApiMapper;

    @PostMapping(
            value = "/leave-ticket/{userId}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseApi<TicketResponse> createLeaveTicket(
            @PathVariable Long userId,
            @RequestPart("ticketInfo") CreateTicketRequest ticketRequest,
            @RequestPart(value = "evidenceFile", required = false) MultipartFile evidenceFile) {

        // chuyển request từ controller sang command ở dưới core
        CreateTicketCommand requestCommand = mapToTicketCommand(ticketRequest);
        // set file evidence
        requestCommand.setEvidence(evidenceFile);

        LeaveRequestCommand leaveCommand = LeaveRequestCommand.builder().build();

        LeaveRequestModel leaveModel =
                ticketUseCaseImpl.createLeaveRequest(requestCommand, leaveCommand, userId);

        return ResponseApi.ok(ticketApiMapper.toTicketResponse(leaveModel));
    }

    @PostMapping(value = "remote-ticket/{userid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseApi<TicketResponse> createRemoteTicket(
            @PathVariable Long userid,
            @RequestPart("ticketInfo") CreateTicketRequest ticketRequest,
            @RequestPart("evidenceFile") MultipartFile evidenceFile,
            @RequestPart("remoteTicketInfo") RemoteTicketRequest remoteTicketRequest) {
        CreateTicketCommand requestCommand = mapToTicketCommand(ticketRequest);
        requestCommand.setEvidence(evidenceFile);
        RemoteRequestCommand remoteCommand =
                ticketApiMapper.toRemoteRequestCommand(remoteTicketRequest);
        return ResponseApi.ok(
                ticketApiMapper.toTicketResponse(
                        ticketUseCaseImpl.createRemoteRequest(requestCommand, remoteCommand, userid)));
    }

    @PostMapping(value = "/explanation-ticket/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseApi<TicketResponse> createExplanationTicket() {
        return ResponseApi.ok(null);
    }

    private CreateTicketCommand mapToTicketCommand(CreateTicketRequest request) {
        return ticketApiMapper.toTicketCommand(request);
    }

    @PutMapping("/user-approval/{ticketId}")
    public ResponseApi<?> approveRegistrationRequest() {
        return ResponseApi.ok(null);
    }

    @PostMapping("/registration-ticket")
    public ResponseApi<List<ListRegistrationResponse>> listRegistrationTicket(
            @RequestBody FilterRegistrationRequest request
    ) {
        return ResponseApi.ok(ticketUseCaseImpl.listAllRegistrationTicket(request.getKeyword(), request.getTicketStatus()).stream().map(ticketApiMapper::toRegistrationResponse).toList());
    }

    @GetMapping(value = "/first-three-registration-ticket", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseApi<List<FirstThreeRegistrationResponse>> firstThreeRegistrationTicket() {
        return ResponseApi.ok(ticketUseCaseImpl.firstThreeRegistrationTicket().stream().map(ticketApiMapper::toFirstThreeRegistrationResponse).toList());
    }

    @GetMapping("/registration-ticket-detail/{ticketId}")
    public ResponseApi<RegistrationDetailResponse> getDetailRegistrationTicket(@PathVariable Long ticketId) {
        return ResponseApi.ok(ticketApiMapper.toRegistrationDetailResponse(ticketUseCaseImpl.getDetailRegistrationTicket(ticketId)));
    }

    @PutMapping("/approve/{ticketId}")
    public ResponseApi<?> approveRegistrationRequest(@PathVariable Long ticketId) {
        return ResponseApi.ok(ticketUseCaseImpl.approveRegistrationTicketByTicketId(ticketId));
    }

    @PutMapping("/reject/{ticketId}")
    public ResponseApi<?> rejectRegistrationRequest(@PathVariable Long ticketId) {
        return ResponseApi.ok(ticketUseCaseImpl.rejectRegistrationTicketByTicketId(ticketId));
    }

    @GetMapping("/approval")
    public ResponseApi<Integer> allRegistrationTicket() {
        return ResponseApi.ok(ticketUseCaseImpl.allRegistrationTicket());
    }

    @GetMapping("/approved")
    public ResponseApi<Integer> allApprovedRegistrationTicket() {
        return ResponseApi.ok(ticketUseCaseImpl.allApprovedRegistrationTicket());
    }

    @GetMapping("/rejected")
    public ResponseApi<Integer> allRejectedRegistrationTicket() {
        return ResponseApi.ok(ticketUseCaseImpl.allRejectedRegistrationTicket());
    }

    @GetMapping("/pending")
    public ResponseApi<Integer> allPendingRegistrationTicket() {
        return ResponseApi.ok(ticketUseCaseImpl.allPendingRegistrationTicket());
    }
}