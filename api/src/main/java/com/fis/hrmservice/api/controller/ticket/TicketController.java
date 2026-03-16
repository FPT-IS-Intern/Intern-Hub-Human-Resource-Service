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
import com.intern.hub.starter.security.annotation.Authenticated;
import com.intern.hub.starter.security.annotation.HasPermission;
import com.intern.hub.starter.security.entity.Action;
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
@CrossOrigin(origins = "*")
public class TicketController {

  TicketUseCaseImpl ticketUseCaseImpl;

  TicketApiMapper ticketApiMapper;

  @PostMapping("/registration-ticket")
  @HasPermission(action = Action.READ, resource = "quan-ly-nguoi-dung")
  @Authenticated
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
  @Authenticated
  @HasPermission(action = Action.READ, resource = "quan-ly-nguoi-dung")
  public ResponseApi<List<FirstThreeRegistrationResponse>> firstThreeRegistrationTicket() {
    return ResponseApi.ok(
        ticketUseCaseImpl.firstThreeRegistrationTicket().stream()
            .map(ticketApiMapper::toFirstThreeRegistrationResponse)
            .toList());
  }

  @GetMapping("/registration-ticket-detail/{ticketId}")
  @Authenticated
  @HasPermission(action = Action.READ, resource = "quan-ly-nguoi-dung")
  public ResponseApi<RegistrationDetailResponse> getDetailRegistrationTicket(
      @PathVariable Long ticketId) {
    return ResponseApi.ok(
        ticketApiMapper.toRegistrationDetailResponse(
            ticketUseCaseImpl.getDetailRegistrationTicket(ticketId)));
  }

  @PutMapping("/approve/{ticketId}")
  @Authenticated
  @HasPermission(action = Action.REVIEW, resource = "quan-ly-nguoi-dung")
  public ResponseApi<?> approveRegistrationRequest(
          @PathVariable Long ticketId) {
    return ResponseApi.ok(ticketUseCaseImpl.approveRegistrationTicketByTicketId(ticketId));
  }

  @PutMapping("/reject/{ticketId}")
  @Authenticated
  @HasPermission(action = Action.REVIEW, resource = "quan-ly-nguoi-dung")
  public ResponseApi<?> rejectRegistrationRequest(@PathVariable Long ticketId) {
    return ResponseApi.ok(ticketUseCaseImpl.rejectRegistrationTicketByTicketId(ticketId));
  }

  @PutMapping("/suspend/{ticketId}")
  @Authenticated
  @HasPermission(action = Action.REVIEW, resource = "quan-ly-nguoi-dung")
  public ResponseApi<?> suspendRegistrationRequest(@PathVariable Long ticketId) {
    return ResponseApi.ok(ticketUseCaseImpl.suspendRegistrationTicketByTicketId(ticketId));
  }

  @GetMapping("/approval")
  @Authenticated
  @HasPermission(action = Action.READ, resource = "quan-ly-nguoi-dung")
  public ResponseApi<Integer> allRegistrationTicket() {
    return ResponseApi.ok(ticketUseCaseImpl.allRegistrationTicket());
  }

  @GetMapping("/approved")
  @Authenticated
  @HasPermission(action = Action.READ, resource = "quan-ly-nguoi-dung")
  public ResponseApi<Integer> allApprovedRegistrationTicket() {
    return ResponseApi.ok(ticketUseCaseImpl.allApprovedRegistrationTicket());
  }

  @GetMapping("/rejected")
  @Authenticated
  @HasPermission(action = Action.READ, resource = "quan-ly-nguoi-dung")
  public ResponseApi<Integer> allRejectedRegistrationTicket() {
    return ResponseApi.ok(ticketUseCaseImpl.allRejectedRegistrationTicket());
  }

  @GetMapping("/pending")
  @Authenticated
  @HasPermission(action = Action.READ, resource = "quan-ly-nguoi-dung")
  public ResponseApi<Integer> allPendingRegistrationTicket() {
    return ResponseApi.ok(ticketUseCaseImpl.allPendingRegistrationTicket());
  }
}
