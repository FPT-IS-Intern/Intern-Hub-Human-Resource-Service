package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.user.AvatarModel;
import com.fis.hrmservice.infra.persistence.entity.Avatar;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-10T10:06:37+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class AvatarMapperImpl implements AvatarMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public AvatarModel toModel(Avatar avatar) {
        if ( avatar == null ) {
            return null;
        }

        AvatarModel.AvatarModelBuilder avatarModel = AvatarModel.builder();

        avatarModel.user( userMapper.toModel( avatar.getUser() ) );
        avatarModel.avatarUrl( avatar.getAvatarUrl() );
        avatarModel.fileType( avatar.getFileType() );
        if ( avatar.getFileSize() != null ) {
            avatarModel.fileSize( avatar.getFileSize() );
        }
        avatarModel.status( avatar.getStatus() );

        return avatarModel.build();
    }
}
