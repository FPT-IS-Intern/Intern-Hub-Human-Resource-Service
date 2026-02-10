package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.CreateQuickNoteRequest;
import com.fis.hrmservice.api.dto.response.QuickNoteResponse;
import com.fis.hrmservice.domain.model.user.QuickNoteModel;
import com.fis.hrmservice.domain.usecase.command.quicknote.QuickNoteCommand;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface QuickNoteApiMapper {

  QuickNoteCommand toCommand(CreateQuickNoteRequest request);

  @Mapping(target = "createDate", source = "writeDate", qualifiedByName = "toLocalDate")
  QuickNoteResponse toResponse(QuickNoteModel model);

  List<QuickNoteResponse> toResponse(List<QuickNoteModel> models);

  @Named("toLocalDate")
  static LocalDate toLocalDate(LocalDateTime writeDate) {
    return writeDate != null ? writeDate.toLocalDate() : null;
  }
}
