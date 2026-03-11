package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.FilterRequest;
import com.fis.hrmservice.api.dto.request.RegisterUserRequest;
import com.fis.hrmservice.api.dto.request.UpdateProfileRequest;
import com.fis.hrmservice.api.dto.response.*;
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
  @Mapping(target = "avatar", source = "avatar")
  @Mapping(target = "cv", source = "cv")
  RegisterUserCommand toCommand(RegisterUserRequest request);

  // ===== Response =====
  @Mapping(target = "email", source = "companyEmail")
  @Mapping(target = "avatarUrl", expression = "java(getAvatarUrl(model))")
  @Mapping(target = "cvUrl", expression = "java(getCvUrl(model))")
  @Mapping(target = "positionCode", source = "position.name")
  @Mapping(target = "superVisorId", source = "mentor.userId")
  UserResponse toResponse(UserModel model);

  default String getAvatarUrl(UserModel model) {
    return model.getAvatar() != null ? model.getAvatar().getAvatarUrl() : null;
  }

  default String getCvUrl(UserModel model) {
    return model.getCv() != null ? model.getCv().getCvUrl() : null;
  }

  @Mapping(source = "companyEmail", target = "email")
  @Mapping(source = "position.name", target = "position")
  @Mapping(source = "avatar.avatarUrl", target = "avatarUrl")
  @Mapping(source = "fullName", target = "fullName")
  @Mapping(source = "sysStatus", target = "sysStatus")
  FilterResponse toFilterResponse(UserModel model);

  @Mapping(target = "position", source = "position.name")
  @Mapping(target = "superVisorName", source = "mentor.fullName")
  @Mapping(target = "role", ignore = true) // UserModel chưa có
  @Mapping(target = "scoreAward", ignore = true) // UserModel chưa có
  @Mapping(target = "budgetPoints", ignore = true)
  // UserModel chưa có
  ProfileResponse toProfileResponse(UserModel model);

  InternalUserProfileResponse toInternalUserProfile(UserModel model);

  List<FilterResponse> toFilterResponseList(List<UserModel> userModelList);

  UpdateUserProfileCommand toUpdateUserProfileCommand(UpdateProfileRequest request);

  @Mapping(target = "email", source = "companyEmail")
  @Mapping(target = "avatarUrl", expression = "java(getAvatarUrl(model))")
  @Mapping(target = "role", ignore = true)
  InternalUserResponse toInternalUserResponse(UserModel model);

  @Mapping(target = "fullName", source = "fullName")
  @Mapping(target = "nickName", source = "fullName", qualifiedByName = "buildNickName")
  @Mapping(target = "avatarUrl", source = "avatar.avatarUrl")
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
  @Mapping(target = "avatarUrl", source = "avatar.avatarUrl")
  @Mapping(target = "fullName", source = "fullName")
  @Mapping(source = "sysStatus", target = "sysStatus")
  @Mapping(target = "companyEmail", source = "companyEmail")
  @Mapping(target = "role", ignore = true)
  @Mapping(target = "position", source = "position.name")
  SupervisorMemberResponse toSupervisorMemberResponse(UserModel model);
}
