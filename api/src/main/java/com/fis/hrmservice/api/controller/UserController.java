package com.fis.hrmservice.api.controller;

import com.fis.hrmservice.api.dto.request.FilterRequest;
import com.fis.hrmservice.api.dto.request.RegisterUserRequest;
import com.fis.hrmservice.api.dto.response.FilterResponse;
import com.fis.hrmservice.api.dto.response.UserResponse;
import com.fis.hrmservice.api.mapper.UserApiMapper;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.input.FilterUserUseCase;
import com.fis.hrmservice.domain.port.input.RegisterUserUseCase;
import com.fis.hrmservice.domain.usecase.command.FilterUserCommand;
import com.fis.hrmservice.domain.usecase.command.RegisterUserCommand;
import com.intern.hub.library.common.annotation.EnableGlobalExceptionHandler;
import com.intern.hub.library.common.dto.ResponseApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@EnableGlobalExceptionHandler
@Slf4j
@Tag(name = "User Management", description = "APIs for user registration and management")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final FilterUserUseCase filterUserUseCase;
    private final UserApiMapper userApiMapper;

    public UserController(RegisterUserUseCase registerUserUseCase, FilterUserUseCase filterUserUseCase, UserApiMapper userApiMapper) {
        this.registerUserUseCase = registerUserUseCase;
        this.filterUserUseCase = filterUserUseCase;
        this.userApiMapper = userApiMapper;
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseApi<?> registerUser(
            @Valid @ModelAttribute RegisterUserRequest request) {

        log.info("Received registration request for email: {}", request.getEmail());

        RegisterUserCommand command = userApiMapper.toCommand(request);

        UserModel registeredUser = registerUserUseCase.registerUser(command);

        UserResponse response = userApiMapper.toResponse(registeredUser);

        log.info("Successfully registered user with ID: {}", registeredUser.getUserId());

        return ResponseApi.ok(response);
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

    @GetMapping("/filter")
    public ResponseApi<List<FilterResponse>> filterUsers(
            @RequestBody FilterRequest request
            ){
        FilterUserCommand filterUserCommand = userApiMapper.toCommand(request);
        List<UserModel> userModelList = filterUserUseCase.filterUsers(filterUserCommand);
        return ResponseApi.ok(userApiMapper.toFilterResponseList(userModelList));
    }
}
