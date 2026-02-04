package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.CreateQuickNoteRequest;
import com.fis.hrmservice.domain.usecase.command.quicknote.QuickNoteCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuickNoteApiMapper {

  QuickNoteCommand toCommand(CreateQuickNoteRequest request);
}
