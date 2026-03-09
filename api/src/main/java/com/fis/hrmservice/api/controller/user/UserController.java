package com.fis.hrmservice.api.controller.user;

import com.fis.hrmservice.api.dto.request.FilterRequest;
import com.fis.hrmservice.api.dto.request.RegisterUserRequest;
import com.fis.hrmservice.api.dto.request.UpdateProfileRequest;
import com.fis.hrmservice.api.dto.response.FilterResponse;
import com.fis.hrmservice.api.dto.response.InternalUserResponse;
import com.fis.hrmservice.api.dto.response.SupervisorResponse;
import com.fis.hrmservice.api.dto.response.UserResponse;
import com.fis.hrmservice.api.mapper.UserApiMapper;
import com.fis.hrmservice.api.util.UserContext;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.feign.CreateAuthIdentityPort;
import com.fis.hrmservice.domain.usecase.command.user.FilterUserCommand;
import com.fis.hrmservice.domain.usecase.command.user.RegisterFaceCommand;
import com.fis.hrmservice.domain.usecase.command.user.RegisterUserCommand;
import com.fis.hrmservice.domain.usecase.implement.user.*;
import com.intern.hub.library.common.annotation.EnableGlobalExceptionHandler;
import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.Authenticated;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("hrm/users")
@EnableGlobalExceptionHandler
@Slf4j
@Tag(name = "User Management", description = "APIs for user registration and management")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4205")
public class UserController {

  RegisterUserUseCaseImpl registerUserUseCase;

  FilterUseCaseImpl filterUserUseCase;

  UserApiMapper userApiMapper;

  UserProfileUseCaseImpl userProfileUseCase;

  UserApproval approvalUser;

  UserRejection rejectionUser;

  UserSuspension userSuspension;

  CreateAuthIdentityPort createAuthIdentityPort;

  SupervisorUseCaseImpl supervisorUseCase;

  RegisterFaceUseCaseImpl registerFaceUseCase;

  @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseApi<?> registerUser(
      @RequestPart("userInfo") RegisterUserRequest request,
      @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile,
      @RequestPart(value = "cvFile", required = false) MultipartFile cvFile) {

    if (avatarFile != null && !avatarFile.isEmpty()) {
      request.setAvatar(avatarFile);
    }
    if (cvFile != null && !cvFile.isEmpty()) {
      request.setCv(cvFile);
    }

    RegisterUserCommand command = userApiMapper.toCommand(request);
    UserModel user = registerUserUseCase.registerUser(command);

    return ResponseApi.ok(userApiMapper.toResponse(user));
  }

  @PostMapping("/filter")
  public ResponseApi<PaginatedData<FilterResponse>> filterUsers(
      @RequestBody FilterRequest request,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    FilterUserCommand command = userApiMapper.toCommand(request);

    PaginatedData<UserModel> result = filterUserUseCase.filterUsers(command, page, size);

    return ResponseApi.ok(
        PaginatedData.<FilterResponse>builder()
            .items(userApiMapper.toFilterResponseList((List<UserModel>) result.getItems()))
            .totalItems(result.getTotalItems())
            .totalPages(result.getTotalPages())
            .build());
  }

  // cái này dùng cho admin xem profile của 1 user cụ thể nào đó
  @GetMapping("/admin/profile/{userId}")
  public ResponseApi<?> adminGetUserProfile(@PathVariable Long userId) {
    log.info("Get user profile for ID: {}", userId);
    UserModel userModel = userProfileUseCase.getUserProfile(userId);
    return ResponseApi.ok(userApiMapper.toResponse(userModel));
  }

  // cái này dùng để cho user xem chính user profile của mình
  @GetMapping("/profile")
  @Authenticated
  public ResponseApi<?> userGetUserProfile() {
    Long userId = UserContext.requiredUserId();
    UserModel userModel = userProfileUseCase.getUserProfile(userId);
    return ResponseApi.ok(userApiMapper.toResponse(userModel));
  }

  // -------------------- Approval and Rejection Endpoints -------------------//
  @PutMapping("/approval/{userId}")
  public ResponseApi<?> approveUser(@PathVariable Long userId) {
    log.info("Approve user request for ID: {}", userId);
    UserModel userModel = approvalUser.approveUser(userId);
    return ResponseApi.ok(
        "Đã approve user " + userModel.getFullName() + " với status: " + userModel.getSysStatus());
  }

  @PutMapping("/rejection/{userId}")
  public ResponseApi<?> rejectUser(@PathVariable Long userId) {
    log.info("Reject user request for ID: {}", userId);
    UserModel userReject = rejectionUser.rejectUser(userId);
    return ResponseApi.ok(
        "Đã reject user " + userReject.getFullName() + " với status: " + userReject.getSysStatus());
  }

  // =====================================================================================
  @PatchMapping(value = "/me/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Authenticated
  public ResponseApi<?> updateProfile(
      @Valid @RequestPart("userInfo") UpdateProfileRequest request,
      @RequestPart(value = "cvFile", required = false) MultipartFile cvFile,
      @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile) {

    Long userId = UserContext.requiredUserId();

    request.setCvFile(cvFile);
    request.setAvatarFile(avatarFile);

    UserModel model = userProfileUseCase.updateProfileUser(userApiMapper.toUpdateUserProfileCommand(request), userId);

    if (model == null) {
      return ResponseApi.ok("Update user profile không thành công");
    } 
    return ResponseApi.ok("Update user profile thành công");
  }

  @PatchMapping(value = "/profile/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Authenticated
  public ResponseApi<?> updateUserProfile(
          @Valid @RequestPart("userInfo") UpdateProfileRequest request,
          @RequestPart(value = "cvFile", required = false) MultipartFile cvFile,
          @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile,
          @PathVariable String userId) {
    Long userIdParse;

    try {
      userIdParse = Long.parseLong(userId);
    } catch (NumberFormatException e) {
        return ResponseApi.ok("User ID không hợp lệ");
    }

    request.setCvFile(cvFile);
    request.setAvatarFile(avatarFile);

    UserModel model = userProfileUseCase.updateProfileUser(userApiMapper.toUpdateUserProfileCommand(request), userIdParse);

    if (model == null) {
      return ResponseApi.ok("Update user profile không thành công");
    }
    return ResponseApi.ok("Update user profile thành công");
  }

  @PutMapping("/suspension/{userId}")
  public ResponseApi<UserResponse> suspendUser(@PathVariable Long userId) {
    UserModel userModel = userSuspension.suspendUser(userId);
    return ResponseApi.ok(userApiMapper.toResponse(userModel));
  }

  @GetMapping("/total-intern")
  public ResponseApi<Integer> totalInternship() {
    Integer totalIntern = approvalUser.totalIntern();
    return ResponseApi.ok(totalIntern);
  }

  @GetMapping("/internship-changing")
  public ResponseApi<String> internshipChanging() {
    Integer internshipChanging = approvalUser.internshipChanging();

    String message;

    if (internshipChanging > 0) {
      message = "↗ " + internshipChanging + " So với tháng trước";
    } else if (internshipChanging < 0) {
      message = "↘ " + Math.abs(internshipChanging) + " So với tháng trước";
    } else {
      message = "→ 0 So với tháng trước";
    }

    return ResponseApi.ok(message);
  }

  @GetMapping("/me")
  @Authenticated
  public ResponseApi<InternalUserResponse> getMeInternal() {
    Long userId = UserContext.requiredUserId();
    UserModel userModel = userProfileUseCase.internalUserProfile(userId);
    return ResponseApi.ok(userApiMapper.toInternalUserResponse(userModel));
  }

  @GetMapping("/supervisor")
  public ResponseApi<List<SupervisorResponse>> listAllSupervisor() {
    return ResponseApi.ok(
        supervisorUseCase.listAllSupervisor().stream()
            .map(userApiMapper::toSupervisorResponse)
            .toList());
  }

  @PostMapping(value = "/me/face-registry", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Authenticated
  public ResponseApi<String> registerFace(
      @RequestParam("userName") String userName,
      @RequestPart("files") List<MultipartFile> files) {

    if (files == null || files.size() != 9) {
      return ResponseApi.ok("Vui lòng cung cấp đúng 9 ảnh khuôn mặt");
    }

    Long userId = UserContext.requiredUserId();

    RegisterFaceCommand command = RegisterFaceCommand.builder()
        .userId(userId)
        .userName(userName)
        .files(files)
        .build();

    UserModel result = registerFaceUseCase.registerFace(command);

    if (Boolean.TRUE.equals(result.getIsFaceRegistry())) {
      return ResponseApi.ok("Đăng ký khuôn mặt thành công");
    }
    return ResponseApi.ok("Đăng ký khuôn mặt thất bại, vui lòng thử lại");
  }
}
