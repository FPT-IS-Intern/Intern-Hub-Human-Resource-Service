package com.fis.hrmservice.api.controller;

import com.fis.hrmservice.api.dto.request.RegisterUserRequest;
import com.fis.hrmservice.api.dto.response.UserResponse;
import com.fis.hrmservice.api.mapper.UserApiMapper;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.input.RegisterUserUseCase;
import com.fis.hrmservice.domain.usecase.command.RegisterUserCommand;
import com.intern.hub.library.common.annotation.EnableGlobalExceptionHandler;
import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.EnableSecurity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@EnableSecurity
@RequiredArgsConstructor
@EnableGlobalExceptionHandler
@Slf4j
@Tag(name = "User Management", description = "APIs for user registration and management")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final UserApiMapper userApiMapper;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseApi<?> registerUser(
            @Valid @ModelAttribute RegisterUserRequest request) {

        log.info("Received registration request for email: {}", request.getEmail());

        // Map API request to domain command using MapStruct
        RegisterUserCommand command = userApiMapper.toCommand(request);

        // Execute use case
        UserModel registeredUser = registerUserUseCase.registerUser(command);

        // Map domain model to response using MapStruct
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
    public ResponseApi<?> filterUsers(
            @RequestParam(required = false) String positionCode,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String departmentCode) {
        log.info("Filter users with positionCode: {} and status: {}", positionCode, status);
        return ResponseApi.ok(null);
    }
}
