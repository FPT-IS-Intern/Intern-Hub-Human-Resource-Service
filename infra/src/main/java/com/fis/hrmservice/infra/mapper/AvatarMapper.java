package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.user.AvatarModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.infra.persistence.entity.Avatar;
import com.fis.hrmservice.infra.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AvatarMapper {

  @Mapping(target = "avatarId", source = "id")
  @Mapping(target = "user", source = "user", qualifiedByName = "userToSimpleModel")
  AvatarModel toModel(Avatar avatar);

  @Mapping(target = "id", source = "avatarId")
  @Mapping(target = "fileType", source = "fileType")
  @Mapping(target = "fileSize", source = "fileSize")
  @Mapping(target = "user", source = "user", qualifiedByName = "userModelToEntity")
  @Mapping(target = "createdAt", source = "createdAt")
  @Mapping(target = "updatedAt", source = "updatedAt")
  @Mapping(target = "createdBy", source = "createdBy")
  @Mapping(target = "updatedBy", source = "updatedBy")
  @Mapping(target = "version", source = "version")
  Avatar toEntity(AvatarModel avatarModel);

  @Named("userToSimpleModel")
  default UserModel userToSimpleModel(User user) {
    if (user == null) return null;
    return UserModel.builder().userId(user.getId()).build();
  }

  @Named("userModelToEntity")
  default User userModelToEntity(UserModel userModel) {
    if (userModel == null || userModel.getUserId() == null) return null;
    User user = new User();
    user.setId(userModel.getUserId());
    return user;
  }
}
