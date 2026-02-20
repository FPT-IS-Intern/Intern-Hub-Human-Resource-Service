package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.user.QuickNoteModel;
import com.fis.hrmservice.infra.persistence.entity.QuickNote;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring",
    uses = {UserMapper.class})
public interface QuickNoteMapper {

  @Mapping(target = "writeDate", source = "writeDate", qualifiedByName = "localDateTimeToLocalDate")
  @Mapping(target = "isDeleted", ignore = true)
  QuickNote toEntity(QuickNoteModel quickNoteModel);

  @Mapping(target = "writeDate", source = "writeDate", qualifiedByName = "localDateToLocalDateTime")
  QuickNoteModel toModel(QuickNote quickNote);

  List<QuickNoteModel> toModelList(List<QuickNote> quickNotes);

  @Named("localDateTimeToLocalDate")
  default LocalDate localDateTimeToLocalDate(LocalDateTime dateTime) {
    return dateTime != null ? dateTime.toLocalDate() : null;
  }

  @Named("localDateToLocalDateTime")
  default LocalDateTime localDateToLocalDateTime(LocalDate date) {
    return date != null ? date.atStartOfDay() : null;
  }
}
