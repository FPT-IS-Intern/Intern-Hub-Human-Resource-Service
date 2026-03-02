package com.fis.hrmservice.api.controller.quicknote;

import com.fis.hrmservice.api.dto.request.CreateQuickNoteRequest;
import com.fis.hrmservice.api.dto.response.QuickNoteResponse;
import com.fis.hrmservice.api.mapper.QuickNoteApiMapper;
import com.fis.hrmservice.api.util.UserContext;
import com.fis.hrmservice.domain.usecase.command.quicknote.QuickNoteCommand;
import com.fis.hrmservice.domain.usecase.implement.quicknote.QuickNoteUseCaseImpl;
import com.intern.hub.library.common.annotation.EnableGlobalExceptionHandler;
import com.intern.hub.library.common.dto.ResponseApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hrm/quickly-note")
@EnableGlobalExceptionHandler
@Slf4j
@Tag(name = "Quick note Management", description = "APIs for Quick note")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class QuickNoteController {

  QuickNoteUseCaseImpl quickNoteUserUseCase;
  QuickNoteApiMapper quickNoteApiMapper;

  @PostMapping("/{userId}")
  public ResponseApi<?> createTicket(
      @RequestBody CreateQuickNoteRequest request, @PathVariable("userId") Long userId) {
    QuickNoteCommand command = quickNoteApiMapper.toCommand(request);
    return ResponseApi.ok(quickNoteUserUseCase.createQuickNote(command, userId));
  }

  // cái này cho admin xem
  @GetMapping("/{userId}")
  public ResponseApi<List<QuickNoteResponse>> listQuickNoteByUserId(@PathVariable Long userId) {
    return ResponseApi.ok(
        quickNoteApiMapper.toResponse(quickNoteUserUseCase.findAllByUserId(userId)));
  }

  // cái này cho user xem của chonhs bản thân mình
  @GetMapping
  public ResponseApi<List<QuickNoteResponse>> myQuickNote() {
    Long userId = UserContext.requiredUserId();
    return ResponseApi.ok(
        quickNoteApiMapper.toResponse(quickNoteUserUseCase.findAllByUserId(userId)));
  }
}
