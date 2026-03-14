package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.user.CvModel;
import com.fis.hrmservice.infra.persistence.entity.Cv;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-14T15:33:02+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class CvMapperImpl implements CvMapper {

    @Override
    public CvModel toModel(Cv cv) {
        if ( cv == null ) {
            return null;
        }

        CvModel.CvModelBuilder cvModel = CvModel.builder();

        if ( cv.getId() != null ) {
            cvModel.cvId( cv.getId() );
        }
        cvModel.user( userToSimpleModel( cv.getUser() ) );
        cvModel.cvUrl( cv.getCvUrl() );
        cvModel.fileType( cv.getFileType() );
        if ( cv.getFileSize() != null ) {
            cvModel.fileSize( cv.getFileSize() );
        }

        return cvModel.build();
    }

    @Override
    public Cv toEntity(CvModel cvModel) {
        if ( cvModel == null ) {
            return null;
        }

        Cv cv = new Cv();

        cv.setId( cvModel.getCvId() );
        cv.setUser( userModelToEntity( cvModel.getUser() ) );
        cv.setCreatedAt( cvModel.getCreatedAt() );
        cv.setUpdatedAt( cvModel.getUpdatedAt() );
        if ( cvModel.getCreatedBy() != null ) {
            cv.setCreatedBy( Long.parseLong( cvModel.getCreatedBy() ) );
        }
        if ( cvModel.getUpdatedBy() != null ) {
            cv.setUpdatedBy( Long.parseLong( cvModel.getUpdatedBy() ) );
        }
        cv.setVersion( cvModel.getVersion() );
        cv.setCvUrl( cvModel.getCvUrl() );
        cv.setFileSize( cvModel.getFileSize() );
        cv.setFileType( cvModel.getFileType() );

        return cv;
    }
}
