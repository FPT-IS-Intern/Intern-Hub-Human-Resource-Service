package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.infra.persistence.entity.Position;
import com.fis.hrmservice.infra.persistence.entity.User;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.mapstruct.*;
import org.mapstruct.Named;

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
  @Mapping(target = "position", source = "position", qualifiedByName = "positionToEntity")
  @Mapping(target = "dateOfBirth", source = "dateOfBirth")
  User toEntity(UserModel model);

  List<UserModel> toResponseList(List<User> users);

  /* ===================== CUSTOM ===================== */

  @Named("localDateToEpoch")
  default long localDateToEpoch(LocalDate date) {
    return date == null ? 0L : date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  @Named("mentorToModel")
  default UserModel mentorToModel(User mentor) {
    if (mentor == null) return null;
    return UserModel.builder().userId(mentor.getId()).fullName(mentor.getFullName()).build();
  }

  @Named("mentorToEntity")
  default User mentorToEntity(UserModel mentor) {
    if (mentor == null || mentor.getUserId() == null) return null;
    User user = new User();
    user.setId(mentor.getUserId());
    return user;
  }

  @Named("positionToEntity")
  default Position positionToEntity(PositionModel model) {
    if (model == null || model.getPositionId() == null) return null;
    Position p = new Position();
    p.setId(model.getPositionId());
    return p;
  }

  @Named("epochMillisToLocalDate")
  default LocalDate epochMillisToLocalDate(Long value) {
    if (value == null) return null;
    return Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDate();
  }
}
