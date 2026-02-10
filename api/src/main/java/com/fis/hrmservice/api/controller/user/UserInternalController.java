package com.fis.hrmservice.api.controller.user;

import com.fis.hrmservice.api.dto.response.UserResponse;
import com.fis.hrmservice.api.mapper.UserApiMapper;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.implement.user.UserProfileUseCaseImpl;
import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.Internal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hrm-service/internal/users")
public class UserInternalController {

  @Autowired private UserProfileUseCaseImpl userProfileUseCase;
  @Autowired private UserApiMapper userApiMapper;

  @GetMapping("/{userId}")
  @Internal
  public ResponseApi<UserResponse> getUserByIdInternal(@PathVariable Long userId) {
    UserModel userModel = userProfileUseCase.internalUserProfile(userId);
    return ResponseApi.ok(userApiMapper.toResponse(userModel));
  }
}
