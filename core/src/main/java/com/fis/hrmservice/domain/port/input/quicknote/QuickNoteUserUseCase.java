package com.fis.hrmservice.domain.port.input.quicknote;

import com.fis.hrmservice.domain.model.user.QuickNoteModel;
import com.fis.hrmservice.domain.usecase.command.quicknote.QuickNoteCommand;

public interface QuickNoteUserUseCase {
  QuickNoteModel createQuickNote(QuickNoteCommand command, Long userId);
}
