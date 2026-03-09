package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.user.QuickNoteModel;
import com.fis.hrmservice.infra.persistence.entity.QuickNote;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {UserMapper.class})
public interface QuickNoteMapper {

  @Mapping(target = "isDeleted", ignore = true)
  QuickNote toEntity(QuickNoteModel quickNoteModel);

  QuickNoteModel toModel(QuickNote quickNote);

  List<QuickNoteModel> toModelList(List<QuickNote> quickNotes);
}
