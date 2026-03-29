package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.utils.response.InternalUploadDirectResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-29T16:00:34+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class InternalUploadDirectMapperInfraImpl implements InternalUploadDirectMapperInfra {

    @Override
    public InternalUploadDirectResponse toDomain(com.fis.hrmservice.infra.model.InternalUploadDirectResponse source) {
        if ( source == null ) {
            return null;
        }

        InternalUploadDirectResponse.InternalUploadDirectResponseBuilder internalUploadDirectResponse = InternalUploadDirectResponse.builder();

        internalUploadDirectResponse.id( source.getId() );
        internalUploadDirectResponse.objectKey( source.getObjectKey() );
        internalUploadDirectResponse.originalFileName( source.getOriginalFileName() );
        internalUploadDirectResponse.contentType( source.getContentType() );
        internalUploadDirectResponse.fileSize( source.getFileSize() );
        internalUploadDirectResponse.status( source.getStatus() );
        internalUploadDirectResponse.actorId( source.getActorId() );
        internalUploadDirectResponse.version( source.getVersion() );
        internalUploadDirectResponse.createdAt( source.getCreatedAt() );
        internalUploadDirectResponse.updatedAt( source.getUpdatedAt() );

        return internalUploadDirectResponse.build();
    }
}
