package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.user.QuickNoteModel;
import com.fis.hrmservice.infra.persistence.entity.QuickNote;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-29T16:00:34+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class QuickNoteMapperImpl implements QuickNoteMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public QuickNote toEntity(QuickNoteModel quickNoteModel) {
        if ( quickNoteModel == null ) {
            return null;
        }

        QuickNote quickNote = new QuickNote();

        quickNote.setContent( quickNoteModel.getContent() );
        quickNote.setCreatedAt( quickNoteModel.getCreatedAt() );
        if ( quickNoteModel.getCreatedBy() != null ) {
            quickNote.setCreatedBy( Long.parseLong( quickNoteModel.getCreatedBy() ) );
        }
        quickNote.setId( quickNoteModel.getId() );
        quickNote.setIntern( userMapper.toEntity( quickNoteModel.getIntern() ) );
        quickNote.setStatus( quickNoteModel.getStatus() );
        quickNote.setUpdatedAt( quickNoteModel.getUpdatedAt() );
        if ( quickNoteModel.getUpdatedBy() != null ) {
            quickNote.setUpdatedBy( Long.parseLong( quickNoteModel.getUpdatedBy() ) );
        }
        quickNote.setVersion( quickNoteModel.getVersion() );
        quickNote.setWriteDate( quickNoteModel.getWriteDate() );
        quickNote.setWriter( userMapper.toEntity( quickNoteModel.getWriter() ) );

        return quickNote;
    }

    @Override
    public QuickNoteModel toModel(QuickNote quickNote) {
        if ( quickNote == null ) {
            return null;
        }

        QuickNoteModel.QuickNoteModelBuilder quickNoteModel = QuickNoteModel.builder();

        quickNoteModel.id( quickNote.getId() );
        quickNoteModel.intern( userMapper.toModel( quickNote.getIntern() ) );
        quickNoteModel.writer( userMapper.toModel( quickNote.getWriter() ) );
        quickNoteModel.content( quickNote.getContent() );
        quickNoteModel.writeDate( quickNote.getWriteDate() );

        return quickNoteModel.build();
    }

    @Override
    public List<QuickNoteModel> toModelList(List<QuickNote> quickNotes) {
        if ( quickNotes == null ) {
            return null;
        }

        List<QuickNoteModel> list = new ArrayList<QuickNoteModel>( quickNotes.size() );
        for ( QuickNote quickNote : quickNotes ) {
            list.add( toModel( quickNote ) );
        }

        return list;
    }
}
