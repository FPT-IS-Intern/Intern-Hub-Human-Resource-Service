package com.fis.hrmservice.api.controller.internal;

import com.fis.hrmservice.api.dto.response.InternalUserResponse;
import com.fis.hrmservice.api.dto.response.UserResponse;
import com.fis.hrmservice.api.mapper.UserApiMapper;
import com.fis.hrmservice.api.util.UserContext;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.implement.user.UserProfileUseCaseImpl;
import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.starter.security.annotation.Internal;

import java.util.List;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hrm/internal/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserInternalController {

    UserProfileUseCaseImpl userProfileUseCase;
    UserApiMapper userApiMapper;

    @GetMapping("/{userId}")
    @Internal
    public ResponseApi<UserResponse> getUserByIdInternal(@PathVariable Long userId) {
        UserModel userModel = userProfileUseCase.internalUserProfile(userId);
        return ResponseApi.ok(userApiMapper.toResponse(userModel));
    }

    @GetMapping("/me")
    public ResponseApi<InternalUserResponse> getMeInternal() {
        Long userId = UserContext.requiredUserId();
        UserModel userModel = userProfileUseCase.internalUserProfile(userId);
        return ResponseApi.ok(userApiMapper.toInternalUserResponse(userModel));
    }

    @GetMapping("/by-email")
    @Internal
    public ResponseApi<UserResponse> getUserByEmailInternal(@RequestParam("email") String email) {
        UserModel userModel = userProfileUseCase.internalUserProfileByEmail(email);
        return ResponseApi.ok(userApiMapper.toResponse(userModel));
    }

    @PostMapping("/by-ids")
    @Internal
    public ResponseApi<List<UserResponse>> getUsersByIdsInternal(@RequestBody List<Long> userIds) {
        if (userIds == null || userIds.isEmpty())
            throw new BadRequestException("user.ids.required", "userIds không được để trống");

        var users = userProfileUseCase.internalUserProfilesByIds(userIds)
                .stream()
                .map(userApiMapper::toResponse)
                .toList();

        return ResponseApi.ok(users);
    }
}
