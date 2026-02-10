package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.CreateQuickNoteRequest;
import com.fis.hrmservice.domain.usecase.command.quicknote.QuickNoteCommand;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-10T10:06:38+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class QuickNoteApiMapperImpl implements QuickNoteApiMapper {

    @Override
    public QuickNoteCommand toCommand(CreateQuickNoteRequest request) {
        if ( request == null ) {
            return null;
        }

        QuickNoteCommand quickNoteCommand = new QuickNoteCommand();

        quickNoteCommand.setContent( request.getContent() );

        return quickNoteCommand;
    }
}
