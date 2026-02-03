package com.fis.hrmservice.domain.port.output;

import com.fis.hrmservice.domain.model.user.QuickNoteModel;

public interface QuickNoteRepositoryPort {
    QuickNoteModel save(QuickNoteModel quickNote);
}
