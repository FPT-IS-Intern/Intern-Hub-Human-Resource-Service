package com.fis.hrmservice.api.controller.user;

import com.fis.hrmservice.api.dto.request.FilterRequest;
import com.fis.hrmservice.api.dto.request.RegisterUserRequest;
import com.fis.hrmservice.api.dto.request.UpdateProfileRequest;
import com.fis.hrmservice.api.dto.response.FilterResponse;
import com.fis.hrmservice.api.mapper.UserApiMapper;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.input.user.*;
import com.fis.hrmservice.domain.usecase.command.FilterUserCommand;
import com.fis.hrmservice.domain.usecase.command.RegisterUserCommand;
import com.intern.hub.library.common.annotation.EnableGlobalExceptionHandler;
import com.intern.hub.library.common.dto.ResponseApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("hrm-serice/api/users")
@RequiredArgsConstructor
@EnableGlobalExceptionHandler
@Slf4j
@Tag(name = "User Management", description = "APIs for user registration and management")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final FilterUserUseCase filterUserUseCase;
    private final UserApiMapper userApiMapper;
    private final UserProfileUseCase userProfileUseCase;
    private final ApprovalUser approvalUser;
    private final RejectionUser rejectionUser;

    @PostMapping(
            value = "/register",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseApi<?> registerUser(
            @RequestPart("userInfo") RegisterUserRequest request,
            @RequestPart("avatarFile") MultipartFile avatarFile,
            @RequestPart("cvFile") MultipartFile cvFile
    ) {

        log.info("Got avatar file: {}, cv file: {}", avatarFile.getOriginalFilename(), cvFile.getOriginalFilename());

        request.setAvatar(avatarFile);
        request.setCv(cvFile);

        RegisterUserCommand command = userApiMapper.toCommand(request);
        UserModel user = registerUserUseCase.registerUser(command);

        return ResponseApi.ok(userApiMapper.toResponse(user));
    }

    @GetMapping("/{userId}")
    public ResponseApi<?> getUserById(@PathVariable Long userId) {
        log.info("Get user request for ID: {}", userId);
        return ResponseApi.ok(null);
    }

    @PutMapping("/{userId}")
    public ResponseApi<?> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody RegisterUserRequest request) {
        log.info("Update user request for ID: {}", userId);
        return ResponseApi.ok(null);
    }

    @DeleteMapping("/{userId}")
    public ResponseApi<?> deactivateUser(@PathVariable Long userId) {
        log.info("Deactivate user request for ID: {}", userId);
        return ResponseApi.ok(null);
    }

    @PostMapping("/filter")
    public ResponseApi<List<FilterResponse>> filterUsers(
            @RequestBody FilterRequest request
            ){
        FilterUserCommand filterUserCommand = userApiMapper.toCommand(request);
        List<UserModel> userModelList = filterUserUseCase.filterUsers(filterUserCommand);
        return ResponseApi.ok(userApiMapper.toFilterResponseList(userModelList));
    }

    //cái này dùng cho admin xem profile của 1 user cụ thể nào đó
    @GetMapping("/profile/{userId}")
    public ResponseApi<?> getUserProfile(@PathVariable("userId") Long userId) {
        UserModel userModel = userProfileUseCase.getUserProfile(userId);
        return ResponseApi.ok(userApiMapper.toResponse(userModel));
    }
    //-------------------- Approval and Rejection Endpoints -------------------//
    @PutMapping("/approval/{userId}")
    @Transactional
    public ResponseApi<?> approveUser(@PathVariable("userId") Long userId) {
        log.info("Approve user request for ID: {}", userId);
        UserModel userModel = approvalUser.approveUser(userId);
        return ResponseApi.ok("Đã approve user " + userModel.getFullName() + " với status: " + userModel.getStatus());
    }

    @PutMapping("/rejection/{userId}")
    @Transactional
    public ResponseApi<?> rejectUser(@PathVariable("userId") Long userId) {
        log.info("Reject user request for ID: {}", userId);
        UserModel userReject = rejectionUser.rejectUser(userId);
        return ResponseApi.ok("Đã reject user " + userReject.getFullName() + " với status: " + userReject.getStatus());
    }

    @PatchMapping(
            value = "/me/{userId}/profile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseApi<?> updateProfile(
            @RequestPart("userInfo")UpdateProfileRequest request,
            @RequestPart("cvFile") MultipartFile cvFile,
            @RequestPart("avatarFile") MultipartFile avatarFile,
            @PathVariable("userId") long userId
            ) {
        request.setCvFile(cvFile);
        request.setAvatarFile(avatarFile);
        userProfileUseCase.updateProfileUser(userApiMapper.toUpdateUserProfileCommand(request), userId);
        return ResponseApi.ok("Update user profile thành công");
    }
}
