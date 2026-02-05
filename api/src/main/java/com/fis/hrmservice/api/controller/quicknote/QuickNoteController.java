package com.fis.hrmservice.api.controller.quicknote;

import com.fis.hrmservice.api.dto.request.CreateQuickNoteRequest;
import com.fis.hrmservice.api.mapper.QuickNoteApiMapper;
import com.fis.hrmservice.domain.usecase.command.quicknote.QuickNoteCommand;
import com.fis.hrmservice.domain.usecase.implement.quicknote.QuickNoteUseCaseImpl;
import com.intern.hub.library.common.annotation.EnableGlobalExceptionHandler;
import com.intern.hub.library.common.dto.ResponseApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hrm-serice/api/quickly-note")
@EnableGlobalExceptionHandler
@Slf4j
@Tag(name = "Quick note Management", description = "APIs for Quick note")
public class QuickNoteController {

  @Autowired
  private QuickNoteUseCaseImpl quickNoteUserUseCase;
  @Autowired
  private QuickNoteApiMapper quickNoteApiMapper;

  @PostMapping("/{userId}")
  public ResponseApi<?> createTicket(
      @RequestBody CreateQuickNoteRequest request, @PathVariable("userId") Long userId) {
    QuickNoteCommand command = quickNoteApiMapper.toCommand(request);
    return ResponseApi.ok(quickNoteUserUseCase.createQuickNote(command, userId));
  }
}
