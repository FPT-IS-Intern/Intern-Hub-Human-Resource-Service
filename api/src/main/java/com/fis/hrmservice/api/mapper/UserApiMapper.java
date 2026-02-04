package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.FilterRequest;
import com.fis.hrmservice.api.dto.request.RegisterUserRequest;
import com.fis.hrmservice.api.dto.request.UpdateProfileRequest;
import com.fis.hrmservice.api.dto.response.FilterResponse;
import com.fis.hrmservice.api.dto.response.ProfileResponse;
import com.fis.hrmservice.api.dto.response.UserResponse;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.command.user.FilterUserCommand;
import com.fis.hrmservice.domain.usecase.command.user.RegisterUserCommand;
import com.fis.hrmservice.domain.usecase.command.user.UpdateUserProfileCommand;
import java.util.List;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserApiMapper {

  // ===== Register =====
  @Mapping(target = "cvFileName", expression = "java(getFileName(request.getCv()))")
  @Mapping(target = "cvContentType", expression = "java(getContentType(request.getCv()))")
  @Mapping(target = "cvSize", expression = "java(getFileSize(request.getCv()))")
  @Mapping(target = "avatarFileName", expression = "java(getFileName(request.getAvatar()))")
  @Mapping(target = "avatarContentType", expression = "java(getContentType(request.getAvatar()))")
  @Mapping(target = "avatarSize", expression = "java(getFileSize(request.getAvatar()))")
  RegisterUserCommand toCommand(RegisterUserRequest request);

  // ===== Response =====
  @Mapping(source = "companyEmail", target = "email")
  @Mapping(source = "position.name", target = "positionCode")
  UserResponse toResponse(UserModel model);

  @Mapping(source = "companyEmail", target = "email")
  @Mapping(source = "position.name", target = "position")
  @Mapping(source = "avatarUrl", target = "avatarUrl")
  @Mapping(source = "fullName", target = "fullName")
  @Mapping(source = "sysStatus", target = "sysStatus")
  FilterResponse toFilterResponse(UserModel model);

  @Mapping(target = "position", source = "position.name")
  @Mapping(target = "superVisorName", source = "mentor.fullName")
  @Mapping(target = "role", ignore = true) // UserModel chưa có
  @Mapping(target = "scoreAward", ignore = true) // UserModel chưa có
  @Mapping(target = "budgetPoints", ignore = true) // UserModel chưa có
  ProfileResponse toProfileResponse(UserModel model);

  List<FilterResponse> toFilterResponseList(List<UserModel> userModelList);

  UpdateUserProfileCommand toUpdateUserProfileCommand(UpdateProfileRequest request);

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
}
