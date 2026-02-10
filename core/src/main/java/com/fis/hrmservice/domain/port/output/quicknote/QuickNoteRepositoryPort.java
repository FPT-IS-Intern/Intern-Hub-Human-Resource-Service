package com.fis.hrmservice.domain.port.output.quicknote;

import com.fis.hrmservice.domain.model.user.QuickNoteModel;

import java.util.List;

public interface QuickNoteRepositoryPort {
  QuickNoteModel save(QuickNoteModel quickNote);
  List<QuickNoteModel> findAllByUserId(Long userId);
}
