package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.constant.CoreConstant;
import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.dto.request.CreateTicketInternalRequest;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.feign.CreateAuthIdentityPort;
import com.fis.hrmservice.domain.port.output.feign.CreateTicketPort;
import com.fis.hrmservice.domain.port.output.user.*;
import com.fis.hrmservice.domain.service.UserValidationService;
import com.fis.hrmservice.domain.usecase.command.user.UpdateUserProfileCommand;
import com.fis.hrmservice.domain.utils.helper.UpdateHelper;
import com.intern.hub.library.common.exception.NotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileUseCaseImpl {

  UserRepositoryPort userRepositoryPort;
  UserValidationService userValidationService;
  FileStoragePort fileStoragePort;
  PositionRepositoryPort positionRepositoryPort;
  CreateAuthIdentityPort createAuthIdentityPort;
  CreateTicketPort createTicketPort;

  public UserModel getUserProfile(Long userId) {
    return userRepositoryPort
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
  }

  @Transactional
  public UserModel updateProfileUser(UpdateUserProfileCommand command, Long userId) {

    UserModel user = userRepositoryPort.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

    userValidationService.validateUpdate(command);

    Map<String, Object> oldProfile = buildCurrentProfileMap(user);

    boolean userFieldChanged = false;

    userFieldChanged |= UpdateHelper.applyIfChanged(
            command.getFullName(),
            user::getFullName,
            user::setFullName,
            (o, n) -> Objects.equals(trim(o), trim(n)));

    userFieldChanged |= UpdateHelper.applyIfChanged(
            command.getCompanyEmail(),
            user::getCompanyEmail,
            user::setCompanyEmail,
            (o, n) -> Objects.equals(trim(o), trim(n)));

    userFieldChanged |= UpdateHelper.applyIfChanged(
            command.getDateOfBirth(),
            user::getDateOfBirth,
            user::setDateOfBirth,
            Objects::equals);

    userFieldChanged |= UpdateHelper.applyIfChanged(
            command.getIdNumber(),
            user::getIdNumber,
            user::setIdNumber,
            (o, n) -> Objects.equals(trim(o), trim(n)));

    userFieldChanged |= UpdateHelper.applyIfChanged(
            command.getAddress(),
            user::getAddress,
            user::setAddress,
            (o, n) -> Objects.equals(trim(o), trim(n)));

    userFieldChanged |= UpdateHelper.applyIfChanged(
            command.getPhoneNumber(),
            user::getPhoneNumber,
            user::setPhoneNumber,
            (o, n) -> Objects.equals(trim(o), trim(n)));

    // ===== Update Position =====
    if (command.getPosition() != null) {
      Long currentPositionId = user.getPosition() != null ? user.getPosition().getPositionId() : null;
      if (!Objects.equals(command.getPosition(), currentPositionId)) {
        user.setPosition(positionRepositoryPort.findById(command.getPosition())
                .orElseThrow(() -> new NotFoundException("Position not found with id: " + command.getPosition())));
        userFieldChanged = true;
      }
    }

    // ===== Update SysStatus =====
    if (command.getSysStatus() != null && !command.getSysStatus().isBlank()) {

      UserStatus newStatus = UserStatus.valueOf(command.getSysStatus().toUpperCase());
      UserStatus oldStatus = user.getSysStatus();

      if (!Objects.equals(oldStatus, newStatus)) {
        user.setSysStatus(newStatus);
        userFieldChanged = true;

        if (newStatus == UserStatus.SUSPENDED) {
          createAuthIdentityPort.lockAuthIdentity(user.getUserId());
        } else if (newStatus == UserStatus.APPROVED) {
          createAuthIdentityPort.unlockAuthIdentity(user.getUserId());
        }
      }
    }

    // ===== Upload CV =====
    if (command.getCvFile() != null && !command.getCvFile().isEmpty()) {
      String cvObjectKey = fileStoragePort.uploadFile(
              command.getCvFile(),
              "cvs/",
              user.getUserId(),
              20971520L,
              "application/(pdf|vnd\\.openxmlformats-officedocument\\.wordprocessingml\\.document)"
      );

      user.setCvUrl(CoreConstant.S3_PREFIX_URL + cvObjectKey);
      userFieldChanged = true;
    }

    // ===== Upload Avatar =====
    if (command.getAvatarFile() != null && !command.getAvatarFile().isEmpty()) {
      String avatarObjectKey = fileStoragePort.uploadFile(
              command.getAvatarFile(),
              "avatars/",
              user.getUserId(),
              20971520L,
              "image/(png|jpeg|jpg|webp)"
      );

      user.setAvatarUrl(CoreConstant.S3_PREFIX_URL + avatarObjectKey);
      userFieldChanged = true;
    }

    if (userFieldChanged) {
      Map<String, Object> newProfile = buildCurrentProfileMap(user);
      Map<String, Object> payload = new LinkedHashMap<>();
      payload.put("userId", userId);
      payload.put("oldProfile", oldProfile);
      payload.put("newProfile", newProfile);

      CreateTicketInternalRequest ticketRequest = new CreateTicketInternalRequest(
              CoreConstant.PROFILE_UPDATE_TICKET_TYPE_ID,
              payload,
              null);

      createTicketPort.createTicket(userId, ticketRequest, null);
    }

    return user;
  }

  private Map<String, Object> buildCurrentProfileMap(UserModel user) {
    Map<String, Object> profile = new LinkedHashMap<>();
    profile.put("userId", user.getUserId());
    profile.put("fullName", user.getFullName());
    profile.put("companyEmail", user.getCompanyEmail());
    profile.put("dateOfBirth", user.getDateOfBirth() != null ? user.getDateOfBirth().toString() : null);
    profile.put("idNumber", user.getIdNumber());
    profile.put("address", user.getAddress());
    profile.put("phoneNumber", user.getPhoneNumber());
    profile.put("positionId", user.getPosition() != null ? user.getPosition().getPositionId() : null);
    profile.put("sysStatus", user.getSysStatus() != null ? user.getSysStatus().name() : null);
    profile.put("cvUrl", user.getCvUrl());
    profile.put("avatarUrl", user.getAvatarUrl());
    return profile;
  }

  public UserModel internalUserProfile(Long userId) {

    UserModel user = userRepositoryPort.internalUserProfile(userId);

    if (user == null) {
      throw new NotFoundException("User with id: " + userId + " not found");
    }
    return user;
  }

  public UserModel internalUserProfileByEmail(String email) {
    String normalizedEmail = email == null ? null : email.strip().toLowerCase();
    return userRepositoryPort
        .findByEmail(normalizedEmail)
        .orElseThrow(() -> new NotFoundException("User with email: " + email + " not found"));
  }

  public List<UserModel> internalUserProfilesByIds(List<Long> userIds) {
    if (userIds == null || userIds.isEmpty()) {
      return List.of();
    }

    return userIds.stream()
        .filter(Objects::nonNull)
        .distinct()
        .map(userRepositoryPort::findById)
        .flatMap(Optional::stream)
        .toList();
  }

  @Transactional
  public UserModel lockAccountInternal(Long userId) {
    return updateAccountStatusAndAuth(userId, UserStatus.SUSPENDED);
  }

  @Transactional
  public UserModel unlockAccountInternal(Long userId) {
    return updateAccountStatusAndAuth(userId, UserStatus.APPROVED);
  }

  private UserModel updateAccountStatusAndAuth(Long userId, UserStatus targetStatus) {
    UserModel user =
        userRepositoryPort
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

    if (!Objects.equals(user.getSysStatus(), targetStatus)) {
      user.setSysStatus(targetStatus);
      userRepositoryPort.save(user);

      if (targetStatus == UserStatus.SUSPENDED) {
        createAuthIdentityPort.lockAuthIdentity(userId);
      } else if (targetStatus == UserStatus.APPROVED) {
        createAuthIdentityPort.unlockAuthIdentity(userId);
      }
    }

    return userRepositoryPort
        .findById(userId)
        .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
  }

  private static String trim(CharSequence cs) {
    return cs == null ? null : cs.toString().strip();
  }

  public List<Long> getUserIdList() {
    return userRepositoryPort.getAllUserId();
  }

  public List<UserModel> searchUsers(String query) {
    if (query == null || query.isBlank()) {
      return List.of();
    }
    return userRepositoryPort.searchByQuery(query.strip());
  }
}
