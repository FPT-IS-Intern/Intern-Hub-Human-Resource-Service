package com.fis.hrmservice.infra.persistence.adapter.quicknote;

import com.fis.hrmservice.domain.model.user.QuickNoteModel;
import com.fis.hrmservice.domain.port.output.quicknote.QuickNoteRepositoryPort;
import com.fis.hrmservice.infra.mapper.QuickNoteMapper;
import com.fis.hrmservice.infra.persistence.entity.QuickNote;
import com.fis.hrmservice.infra.persistence.repository.quicknote.QuickNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class QuickNoteRepositoryAdapter implements QuickNoteRepositoryPort {

  @Autowired private QuickNoteRepository quickNoteRepository;

  @Autowired private QuickNoteMapper quickNoteMapper;

  @Override
  public QuickNoteModel save(QuickNoteModel quickNoteModel) {

    QuickNote quickNote = quickNoteMapper.toEntity(quickNoteModel);

    quickNoteRepository.save(quickNote);

    return quickNoteMapper.toModel(quickNote);
  }
}
