package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.*;
import com.fis.hrmservice.api.dto.response.*;
import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.command.user.FilterUserCommand;
import com.fis.hrmservice.domain.usecase.command.user.RegisterUserCommand;
import com.fis.hrmservice.domain.usecase.command.user.UpdateUserProfileCommand;
import java.util.List;

import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserApiMapper {

  // ===== Register =====
  RegisterUserCommand toCommand(RegisterUserRequest request);

  // ===== Response =====
  @Mapping(target = "email", source = "companyEmail")
  @Mapping(target = "positionCode", source = "position.name")
  @Mapping(target = "superVisorId", source = "mentor.userId")
  UserResponse toResponse(UserModel model);

  @Mapping(source = "companyEmail", target = "email")
  @Mapping(target = "position", expression = "java(getDisplayPosition(model.getPosition()))")
  @Mapping(target = "role", expression = "java(getDisplayRole(model.getPosition()))")
  @Mapping(source = "avatarUrl", target = "avatarUrl")
  @Mapping(source = "fullName", target = "fullName")
  @Mapping(source = "sysStatus", target = "sysStatus")
  FilterResponse toFilterResponse(UserModel model);

  default String getDisplayRole(PositionModel positionModel) {
    String rawName = positionModel != null ? positionModel.getName() : null;
    if (rawName == null || rawName.isBlank()) {
      return null;
    }

    String normalized = rawName.trim();
    String[] parts = normalized.split("\\s+");
    if (parts.length >= 2) {
      return parts[0];
    }

    String upperName = normalized.toUpperCase();
    if (upperName.startsWith("INTERN") && upperName.length() > 6) {
      return "INTERN";
    }
    if (upperName.startsWith("STAFF") && upperName.length() > 5) {
      return "STAFF";
    }

    return normalized;
  }

  default String getDisplayPosition(PositionModel positionModel) {
    String rawName = positionModel != null ? positionModel.getName() : null;
    if (rawName == null || rawName.isBlank()) {
      return null;
    }

    String normalized = rawName.trim();
    String[] parts = normalized.split("\\s+");
    if (parts.length >= 2) {
      return String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length));
    }

    String upperName = normalized.toUpperCase();
    if (upperName.startsWith("INTERN") && upperName.length() > 6) {
      return normalized.substring(6).trim();
    }
    if (upperName.startsWith("STAFF") && upperName.length() > 5) {
      return normalized.substring(5).trim();
    }

    // One-word codes like PO/PM/PMO belong to role only.
    return null;
  }

  InternalUserProfileResponse toInternalUserProfile(UserModel model);

  List<FilterResponse> toFilterResponseList(List<UserModel> userModelList);

  UpdateUserProfileCommand toUpdateUserProfileCommand(UpdateProfileRequest request);

  @Mapping(target = "email", source = "companyEmail")
  @Mapping(target = "positionName", source = "position.name")
  InternalUserResponse toInternalUserResponse(UserModel model);

  @Mapping(target = "fullName", source = "fullName")
  @Mapping(target = "nickName", source = "fullName", qualifiedByName = "buildNickName")
  @Mapping(target = "avatarUrl", source = "avatarUrl")
  @Mapping(target = "role", ignore = true)
  SupervisorResponse toSupervisorResponse(UserModel model);

  @Named("buildNickName")
  default String buildNickName(String fullName) {
    if (fullName == null || fullName.isBlank()) return null;

    String[] parts = fullName.trim().split("\\s+");

    if (parts.length == 1) return fullName;

    String lastName = parts[parts.length - 1];
    StringBuilder initials = new StringBuilder();

    for (int i = 0; i < parts.length - 1; i++) {
      initials.append(parts[i].charAt(0));
    }

    return lastName + initials;
  }

  // ===== Filter =====
  FilterUserCommand toCommand(FilterRequest request);

  // ===== Helpers =====
  default String getFileName(MultipartFile file) {
    return file != null ? file.getOriginalFilename() : null;
  }

  default String getContentType(MultipartFile file) {
    return file != null ? file.getContentType() : null;
  }

  default long getFileSize(MultipartFile file) {
    return file != null ? file.getSize() : 0L;
  }

  @Mapping(target = "no", ignore = true)
  @Mapping(target = "userId", source = "userId")
  @Mapping(target = "avatarUrl", source = "avatarUrl")
  @Mapping(target = "fullName", source = "fullName")
  @Mapping(source = "sysStatus", target = "sysStatus")
  @Mapping(target = "companyEmail", source = "companyEmail")
  @Mapping(target = "role", ignore = true)
  @Mapping(target = "position", source = "position.name")
  SupervisorMemberResponse toSupervisorMemberResponse(UserModel model);

  @Mapping(target = "id", source = "userId")
  @Mapping(target = "email", source = "companyEmail")
  HrmUserSearchResponse toHrmUserSearchResponse(UserModel model);

  List<HrmUserSearchResponse> toHrmUserSearchResponseList(List<UserModel> models);
}
