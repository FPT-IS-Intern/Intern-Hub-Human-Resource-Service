package com.fis.hrmservice.api.controller.ticket;

import com.fis.hrmservice.api.dto.request.ApproveTicketRequest;
import com.fis.hrmservice.api.dto.request.FilterRegistrationRequest;
import com.fis.hrmservice.api.dto.response.FirstThreeRegistrationResponse;
import com.fis.hrmservice.api.dto.response.ListRegistrationResponse;
import com.fis.hrmservice.api.dto.response.RegistrationDetailResponse;
import com.fis.hrmservice.api.mapper.TicketApiMapper;
import com.fis.hrmservice.domain.model.ticket.TicketModel;
import com.fis.hrmservice.domain.usecase.command.ticket.FilterRegistrationTicketCommand;
import com.fis.hrmservice.domain.usecase.implement.ticket.TicketUseCaseImpl;
import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.dto.ResponseApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hrm/ticket")
@Slf4j
@Tag(name = "Ticket Management", description = "APIs for Ticket")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4205")
public class TicketController {

  TicketUseCaseImpl ticketUseCaseImpl;

  TicketApiMapper ticketApiMapper;

  @PostMapping("/registration-ticket")
  public ResponseApi<PaginatedData<ListRegistrationResponse>> listRegistrationTicket(
      @RequestBody FilterRegistrationRequest request,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    FilterRegistrationTicketCommand command = ticketApiMapper.toCommand(request);

    PaginatedData<TicketModel> result =
        ticketUseCaseImpl.listRegistrationTicketPaged(command, page, size);

    return ResponseApi.ok(
        PaginatedData.<ListRegistrationResponse>builder()
            .items(
                ticketApiMapper.toRegistrationResponseList((List<TicketModel>) result.getItems()))
            .totalItems(result.getTotalItems())
            .totalPages(result.getTotalPages())
            .build());
  }

  @GetMapping(
      value = "/first-three-registration-ticket",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseApi<List<FirstThreeRegistrationResponse>> firstThreeRegistrationTicket() {
    return ResponseApi.ok(
        ticketUseCaseImpl.firstThreeRegistrationTicket().stream()
            .map(ticketApiMapper::toFirstThreeRegistrationResponse)
            .toList());
  }

  @GetMapping("/registration-ticket-detail/{ticketId}")
  public ResponseApi<RegistrationDetailResponse> getDetailRegistrationTicket(
      @PathVariable Long ticketId) {
    return ResponseApi.ok(
        ticketApiMapper.toRegistrationDetailResponse(
            ticketUseCaseImpl.getDetailRegistrationTicket(ticketId)));
  }

  @PutMapping("/approve/{ticketId}")
  public ResponseApi<?> approveRegistrationRequest(
          @PathVariable Long ticketId,
          @RequestBody ApproveTicketRequest request) {
    return ResponseApi.ok(ticketUseCaseImpl.approveRegistrationTicketByTicketId(ticketId, request.getRoleId()));
  }

  @PutMapping("/reject/{ticketId}")
  public ResponseApi<?> rejectRegistrationRequest(@PathVariable Long ticketId) {
    return ResponseApi.ok(ticketUseCaseImpl.rejectRegistrationTicketByTicketId(ticketId));
  }

  @PutMapping("/suspend/{ticketId}")
  public ResponseApi<?> suspendRegistrationRequest(@PathVariable Long ticketId) {
    return ResponseApi.ok(ticketUseCaseImpl.suspendRegistrationTicketByTicketId(ticketId));
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
