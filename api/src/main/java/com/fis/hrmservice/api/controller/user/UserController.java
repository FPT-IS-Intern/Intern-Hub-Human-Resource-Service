package com.fis.hrmservice.api.controller.user;

import com.fis.hrmservice.api.dto.request.FilterRequest;
import com.fis.hrmservice.api.dto.request.RegisterUserRequest;
import com.fis.hrmservice.api.dto.request.UpdateProfileRequest;
import com.fis.hrmservice.api.dto.response.FilterResponse;
import com.fis.hrmservice.api.dto.response.UserResponse;
import com.fis.hrmservice.api.mapper.UserApiMapper;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.command.user.FilterUserCommand;
import com.fis.hrmservice.domain.usecase.command.user.RegisterUserCommand;
import com.fis.hrmservice.domain.usecase.implement.user.*;
import com.intern.hub.library.common.annotation.EnableGlobalExceptionHandler;
import com.intern.hub.library.common.dto.ResponseApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("hrm-service/users")
@EnableGlobalExceptionHandler
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4205"})
@Slf4j
@Tag(name = "User Management", description = "APIs for user registration and management")
public class UserController {

  @Autowired private RegisterUserUseCaseImpl registerUserUseCase;

  @Autowired private FilterUseCaseImpl filterUserUseCase;

  @Autowired private UserApiMapper userApiMapper;

  @Autowired private UserProfileUseCaseImpl userProfileUseCase;

  @Autowired private UserApproval approvalUser;

  @Autowired private UserRejection rejectionUser;

  @Autowired private UserSuspension userSuspension;

  @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseApi<?> registerUser(
      @RequestPart("userInfo") RegisterUserRequest request,
      @RequestPart("avatarFile") MultipartFile avatarFile,
      @RequestPart("cvFile") MultipartFile cvFile) {

    log.info(
        "Got avatar file: {}, cv file: {}",
        avatarFile.getOriginalFilename(),
        cvFile.getOriginalFilename());

    request.setAvatar(avatarFile);
    request.setCv(cvFile);

    RegisterUserCommand command = userApiMapper.toCommand(request);
    UserModel user = registerUserUseCase.registerUser(command);

    return ResponseApi.ok(userApiMapper.toResponse(user));
  }

  @PostMapping("/filter")
  public ResponseApi<List<FilterResponse>> filterUsers(@RequestBody FilterRequest request) {
    FilterUserCommand filterUserCommand = userApiMapper.toCommand(request);
    List<UserModel> userModelList = filterUserUseCase.filterUsers(filterUserCommand);
    return ResponseApi.ok(userApiMapper.toFilterResponseList(userModelList));
  }

  // cái này dùng cho admin xem profile của 1 user cụ thể nào đó
  @GetMapping("/profile/{userId}")
  public ResponseApi<?> getUserProfile(@PathVariable Long userId) {
    log.info("Get user profile for ID: {}", userId);
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
  @PatchMapping(value = "/me/{userId}/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseApi<?> updateProfile(
      @RequestPart("userInfo") UpdateProfileRequest request,
      @RequestPart("cvFile") MultipartFile cvFile,
      @RequestPart("avatarFile") MultipartFile avatarFile,
      @PathVariable long userId) { // sau này hoàn thành api gateway se sửa sau
    log.info("Update profile for user ID: {}", userId);
    request.setCvFile(cvFile);
    request.setAvatarFile(avatarFile);
    userProfileUseCase.updateProfileUser(userApiMapper.toUpdateUserProfileCommand(request), userId);
    return ResponseApi.ok("Update user profile thành công");
  }

  @GetMapping("/internal/{userId}")
  //  @Internal
  public ResponseApi<UserResponse> getUserByIdInternal(@PathVariable Long userId) {
    UserModel userModel = userProfileUseCase.internalUserProfile(userId);
    return ResponseApi.ok(userApiMapper.toResponse(userModel));
  }

  @PutMapping("/suspension/{userId}")
  public ResponseApi<UserResponse> suspendUser(@PathVariable Long userId) {
    UserModel userModel = userSuspension.suspendUser(userId);
    return ResponseApi.ok(userApiMapper.toResponse(userModel));
  }
}
