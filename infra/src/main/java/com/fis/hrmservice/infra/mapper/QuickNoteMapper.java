package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.user.QuickNoteModel;
import com.fis.hrmservice.infra.persistence.entity.QuickNote;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuickNoteMapper {

    QuickNote toEntity(QuickNoteModel quickNoteModel);

    QuickNoteModel toModel(QuickNote quickNote);
}
