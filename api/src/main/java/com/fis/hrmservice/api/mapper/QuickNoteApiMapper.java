package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.CreateQuickNoteRequest;
import com.fis.hrmservice.api.dto.response.QuickNoteResponse;
import com.fis.hrmservice.domain.model.user.QuickNoteModel;
import com.fis.hrmservice.domain.usecase.command.quicknote.QuickNoteCommand;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuickNoteApiMapper {

  QuickNoteCommand toCommand(CreateQuickNoteRequest request);

  @Mapping(target = "createDate", source = "writeDate")
  QuickNoteResponse toResponse(QuickNoteModel model);

  List<QuickNoteResponse> toResponse(List<QuickNoteModel> models);
}
