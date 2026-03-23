package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.CreateQuickNoteRequest;
import com.fis.hrmservice.api.dto.response.QuickNoteResponse;
import com.fis.hrmservice.domain.model.user.QuickNoteModel;
import com.fis.hrmservice.domain.usecase.command.quicknote.QuickNoteCommand;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-22T17:23:39+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
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

    @Override
    public QuickNoteResponse toResponse(QuickNoteModel model) {
        if ( model == null ) {
            return null;
        }

        QuickNoteResponse quickNoteResponse = new QuickNoteResponse();

        quickNoteResponse.setCreateDate( model.getWriteDate() );
        quickNoteResponse.setContent( model.getContent() );

        return quickNoteResponse;
    }

    @Override
    public List<QuickNoteResponse> toResponse(List<QuickNoteModel> models) {
        if ( models == null ) {
            return null;
        }

        List<QuickNoteResponse> list = new ArrayList<QuickNoteResponse>( models.size() );
        for ( QuickNoteModel quickNoteModel : models ) {
            list.add( toResponse( quickNoteModel ) );
        }

        return list;
    }
}
