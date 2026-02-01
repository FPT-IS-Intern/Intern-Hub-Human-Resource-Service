package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.infra.persistence.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /* ===================== ENTITY -> MODEL ===================== */

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "mentor", qualifiedByName = "mentorToModel")
    UserModel toModel(User entity);

    List<UserModel> toModelList(List<User> entities);

    /* ===================== MODEL -> ENTITY ===================== */

    @Mapping(target = "id", source = "userId")
    @Mapping(target = "mentor", qualifiedByName = "mentorToEntity")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    User toEntity(UserModel model);

    /* ===================== CUSTOM ===================== */

    /**
     * Tránh loop mentor -> mentor -> mentor
     * Chỉ map ID
     */
    @Named("mentorToModel")
    default UserModel mentorToModel(User mentor) {
        if (mentor == null) return null;
        return UserModel.builder()
                .userId(mentor.getId())
                .fullName(mentor.getFullName())
                .build();
    }

    @Named("mentorToEntity")
    default User mentorToEntity(UserModel mentor) {
        if (mentor == null || mentor.getUserId() == null) return null;
        User user = new User();
        user.setId(mentor.getUserId());
        return user;
    }

    List<UserModel> toResponseList(List<User> users);
}