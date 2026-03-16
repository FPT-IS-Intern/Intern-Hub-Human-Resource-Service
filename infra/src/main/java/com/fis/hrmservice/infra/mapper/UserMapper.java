package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.constant.CoreConstant;
import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.infra.persistence.entity.Department;
import com.fis.hrmservice.infra.persistence.entity.Position;
import com.fis.hrmservice.infra.persistence.entity.User;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

  /* ===================== ENTITY -> MODEL ===================== */

  @Mapping(target = "userId", source = "id")
  @Mapping(target = "mentor", qualifiedByName = "mentorToModel")
  @Mapping(target = "position", qualifiedByName = "positionToModel")
  @Mapping(target = "department", source = "department", qualifiedByName = "departmentToString")
  UserModel toModel(User entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "mentor", qualifiedByName = "mentorToEntity")
  @Mapping(target = "position", source = "position", qualifiedByName = "positionToEntity")
  @Mapping(target = "department", ignore = true)
  @Mapping(target = "isFaceRegistry", ignore = true)
  @Mapping(target = "isLearner", ignore = true)
  @Mapping(target = "username", ignore = true)
  void updateEntity(UserModel model, @MappingTarget User entity);

  List<UserModel> toModelList(List<User> entities);

  /* ===================== MODEL -> ENTITY ===================== */

  @Mapping(target = "id", source = "userId")
  @Mapping(target = "mentor", qualifiedByName = "mentorToEntity")
  @Mapping(target = "position", source = "position", qualifiedByName = "positionToEntity")
  @Mapping(target = "department", ignore = true)
  @Mapping(target = "isFaceRegistry", ignore = true)
  @Mapping(target = "isLearner", ignore = true)
  @Mapping(target = "username", ignore = true)
  User toEntity(UserModel model);

  List<UserModel> toResponseList(List<User> users);

  /* ===================== CUSTOM ===================== */

  @Named("localDateToEpoch")
  default long localDateToEpoch(LocalDate date) {
    return date == null
        ? 0L
        : date.atStartOfDay(CoreConstant.VIETNAM_ZONE).toInstant().toEpochMilli();
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

  @Named("positionToModel")
  default PositionModel positionToModel(Position entity) {
    if (entity == null) return null;
    return PositionModel.builder()
        .positionId(entity.getId())
        .name(entity.getName())
        .description(entity.getDescription())
        .build();
  }

  @Named("epochMillisToLocalDate")
  default LocalDate epochMillisToLocalDate(Long value) {
    if (value == null) return null;
    return Instant.ofEpochMilli(value).atZone(CoreConstant.VIETNAM_ZONE).toLocalDate();
  }

  @Named("departmentToString")
  default String departmentToString(Department department) {
    if (department == null) return null;
    return department.getName();
  }

  @Named("stringToDepartment")
  default Department stringToDepartment(String name) {
    if (name == null) return null;

    Department d = new Department();
    d.setName(name);
    return d;
  }
}
