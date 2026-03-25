package com.fis.hrmservice.api.controller.internal;

import com.fis.hrmservice.api.dto.request.FilterRequest;
import com.fis.hrmservice.api.dto.request.UpdateProfileRequest;
import com.fis.hrmservice.api.dto.response.FilterResponse;
import com.fis.hrmservice.api.dto.response.HrmUserSearchResponse;
import com.fis.hrmservice.api.dto.response.InternalUserResponse;
import com.fis.hrmservice.api.dto.response.UserResponse;
import com.fis.hrmservice.api.mapper.UserApiMapper;
import com.fis.hrmservice.api.util.UserContext;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.command.user.FilterUserCommand;
import com.fis.hrmservice.domain.usecase.implement.user.FilterUseCaseImpl;
import com.fis.hrmservice.domain.usecase.implement.user.UserProfileUseCaseImpl;
import com.fis.hrmservice.domain.usecase.implement.user.UserSuspension;
import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.starter.security.annotation.Internal;

import java.util.List;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hrm/internal/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserInternalController {

    UserProfileUseCaseImpl userProfileUseCase;
    UserApiMapper userApiMapper;
    FilterUseCaseImpl filterUserUseCase;
    UserSuspension userSuspension;

    @GetMapping("/search")
    @Internal
    public ResponseApi<List<HrmUserSearchResponse>> searchUsers(@RequestParam("query") String query) {
        List<UserModel> users = userProfileUseCase.searchUsers(query);
        return ResponseApi.ok(userApiMapper.toHrmUserSearchResponseList(users));
    }

    @GetMapping("/{userId}")
    @Internal
    public ResponseApi<UserResponse> getUserByIdInternal(@PathVariable Long userId) {
        UserModel userModel = userProfileUseCase.internalUserProfile(userId);
        return ResponseApi.ok(userApiMapper.toResponse(userModel));
    }

    @GetMapping("/get-user-id")
    @Internal
    public ResponseApi<List<Long>> getUserIdList() {
        return ResponseApi.ok(userProfileUseCase.getUserIdList());
    }

    @GetMapping("/me")
    @Internal
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
        if (userIds == null || userIds.isEmpty()) {
            throw new BadRequestException("user.ids.required", "userIds không được để trống");
        }

        var users = userProfileUseCase.internalUserProfilesByIds(userIds)
                .stream()
                .map(userApiMapper::toResponse)
                .toList();

        return ResponseApi.ok(users);
    }

    @PostMapping("internal/filter")
    @Internal
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

    /**
     * Endpoint dành riêng cho Ticket Service — trả về danh sách user rút gọn (id, fullName, email)
     * với phân trang. Dùng khi Ticket Service cần lọc tickets theo nameOrEmail
     * mà không muốn nhận toàn bộ dữ liệu user.
     */
    @PostMapping("internal/filter-for-ticket")
    @Internal
    public ResponseApi<PaginatedData<HrmUserSearchResponse>> filterUsersForTicket(
            @RequestBody FilterRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        FilterUserCommand command = userApiMapper.toCommand(request);
        PaginatedData<UserModel> result = filterUserUseCase.filterUsers(command, page, size);

        return ResponseApi.ok(
                PaginatedData.<HrmUserSearchResponse>builder()
                        .items(userApiMapper.toHrmUserSearchResponseList((List<UserModel>) result.getItems()))
                        .totalItems(result.getTotalItems())
                        .totalPages(result.getTotalPages())
                        .build());
    }

    @PatchMapping(value = "/{userId}/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Internal
    public ResponseApi<UserResponse> updateUserProfileInternal(
            @PathVariable Long userId,
            @RequestBody UpdateProfileRequest request) {
        UserModel userModel = userProfileUseCase.updateProfileUser(userApiMapper.toUpdateUserProfileCommand(request), userId);
        return ResponseApi.ok(userApiMapper.toResponse(userModel));
    }

    @PutMapping("/{userId}/lock")
    @Internal
    public ResponseApi<UserResponse> lockAccountInternal(@PathVariable Long userId) {
        UserModel userModel = userProfileUseCase.lockAccountInternal(userId);
        return ResponseApi.ok(userApiMapper.toResponse(userModel));
    }

    @PutMapping("/{userId}/unlock")
    @Internal
    public ResponseApi<UserResponse> unlockAccountInternal(@PathVariable Long userId) {
        UserModel userModel = userProfileUseCase.unlockAccountInternal(userId);
        return ResponseApi.ok(userApiMapper.toResponse(userModel));
    }

    @PutMapping("/suspension/{userId}")
    @Internal
    public ResponseApi<UserResponse> suspendUserInternal(@PathVariable Long userId) {
        UserModel userModel = userSuspension.suspendUser(userId);
        return ResponseApi.ok(userApiMapper.toResponse(userModel));
    }
}
