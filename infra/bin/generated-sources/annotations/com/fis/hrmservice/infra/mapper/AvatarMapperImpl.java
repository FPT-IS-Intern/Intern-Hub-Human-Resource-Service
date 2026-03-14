package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.user.AvatarModel;
import com.fis.hrmservice.infra.persistence.entity.Avatar;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-14T15:33:03+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class AvatarMapperImpl implements AvatarMapper {

    @Override
    public AvatarModel toModel(Avatar avatar) {
        if ( avatar == null ) {
            return null;
        }

        AvatarModel.AvatarModelBuilder avatarModel = AvatarModel.builder();

        if ( avatar.getId() != null ) {
            avatarModel.avatarId( avatar.getId() );
        }
        avatarModel.user( userToSimpleModel( avatar.getUser() ) );
        avatarModel.avatarUrl( avatar.getAvatarUrl() );
        avatarModel.fileType( avatar.getFileType() );
        if ( avatar.getFileSize() != null ) {
            avatarModel.fileSize( avatar.getFileSize() );
        }

        return avatarModel.build();
    }

    @Override
    public Avatar toEntity(AvatarModel avatarModel) {
        if ( avatarModel == null ) {
            return null;
        }

        Avatar avatar = new Avatar();

        avatar.setId( avatarModel.getAvatarId() );
        avatar.setFileType( avatarModel.getFileType() );
        avatar.setFileSize( avatarModel.getFileSize() );
        avatar.setUser( userModelToEntity( avatarModel.getUser() ) );
        avatar.setCreatedAt( avatarModel.getCreatedAt() );
        avatar.setUpdatedAt( avatarModel.getUpdatedAt() );
        if ( avatarModel.getCreatedBy() != null ) {
            avatar.setCreatedBy( Long.parseLong( avatarModel.getCreatedBy() ) );
        }
        if ( avatarModel.getUpdatedBy() != null ) {
            avatar.setUpdatedBy( Long.parseLong( avatarModel.getUpdatedBy() ) );
        }
        avatar.setVersion( avatarModel.getVersion() );
        avatar.setAvatarUrl( avatarModel.getAvatarUrl() );
        avatar.setStatus( avatarModel.getStatus() );

        return avatar;
    }
}
