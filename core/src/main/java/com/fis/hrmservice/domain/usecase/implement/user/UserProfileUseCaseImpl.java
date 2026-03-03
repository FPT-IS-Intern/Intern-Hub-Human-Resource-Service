package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.user.AvatarModel;
import com.fis.hrmservice.domain.model.user.CvModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.feign.InternalUploadDirectPort;
import com.fis.hrmservice.domain.port.output.user.AvatarRepositoryPort;
import com.fis.hrmservice.domain.port.output.user.CvRepositoryPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.fis.hrmservice.domain.service.UserValidationService;
import com.fis.hrmservice.domain.usecase.command.user.UpdateUserProfileCommand;
import com.fis.hrmservice.domain.utils.helper.UpdateHelper;
import com.fis.hrmservice.domain.utils.response.InternalUploadDirectResponse;
import com.intern.hub.library.common.exception.NotFoundException;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileUseCaseImpl {

  UserRepositoryPort userRepositoryPort;
  UserValidationService userValidationService;
  InternalUploadDirectPort fileUploadPort;
  AvatarRepositoryPort avatarRepositoryPort;
  CvRepositoryPort cvRepositoryPort;

  public UserModel getUserProfile(Long userId) {
    return userRepositoryPort
        .findById(userId)
        .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
  }

  public UserModel updateProfileUser(UpdateUserProfileCommand command, long userId) {

    UserModel user =
        userRepositoryPort
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

    userValidationService.validateUpdate(command);

    boolean changed = false;

    changed |=
        UpdateHelper.applyIfChanged(
            command.getFullName(),
            user::getFullName,
            user::setFullName,
            (o, n) -> Objects.equals(trim(o), trim(n)));

    changed |=
        UpdateHelper.applyIfChanged(
            command.getCompanyEmail(),
            user::getCompanyEmail,
            user::setCompanyEmail,
            (o, n) -> Objects.equals(trim(o), trim(n)));

    changed |=
        UpdateHelper.applyIfChanged(
            command.getDateOfBirth(), user::getDateOfBirth, user::setDateOfBirth, Objects::equals);

    changed |=
        UpdateHelper.applyIfChanged(
            command.getIdNumber(),
            user::getIdNumber,
            user::setIdNumber,
            (o, n) -> Objects.equals(trim(o), trim(n)));

    changed |=
        UpdateHelper.applyIfChanged(
            command.getAddress(),
            user::getAddress,
            user::setAddress,
            (o, n) -> Objects.equals(trim(o), trim(n)));

    changed |=
        UpdateHelper.applyIfChanged(
            command.getPhoneNumber(),
            user::getPhoneNumber,
            user::setPhoneNumber,
            (o, n) -> Objects.equals(trim(o), trim(n)));

    if (command.getCvFile() != null && !command.getCvFile().isEmpty()) {

      InternalUploadDirectResponse cvResult =
          fileUploadPort.upload(command.getCvFile(), "cvs/" + user.getUserId(), user.getUserId());

      CvModel cv = cvRepositoryPort.findByUserId(user.getUserId());

      if (cv == null) {
        cv = new CvModel();
        cv.setUser(user);
      }

      cv.setCvUrl(cvResult.getObjectKey());
      cv.setFileSize(command.getCvFile().getSize());
      cv.setFileType(command.getCvFile().getContentType());
      CvModel savedCv = cvRepositoryPort.save(cv);
      user.setCv(savedCv);
      changed = true;
    }

    if (command.getAvatarFile() != null && !command.getAvatarFile().isEmpty()) {

      InternalUploadDirectResponse avatarResult =
          fileUploadPort.upload(
              command.getAvatarFile(), "avatars/" + user.getUserId(), user.getUserId());

      AvatarModel avatar = avatarRepositoryPort.getAvatarByUserId(user.getUserId());

      if (avatar == null) {
        avatar = new AvatarModel();
        avatar.setUser(user);
      }

      avatar.setAvatarUrl(avatarResult.getObjectKey());
      avatar.setFileSize(command.getAvatarFile().getSize());
      avatar.setFileType(command.getAvatarFile().getContentType());

      AvatarModel savedAvatar = avatarRepositoryPort.save(avatar);
      user.setAvatar(savedAvatar);
      changed = true;
    }

    // Nếu không có thay đổi gì thì return luôn (không throw)
    if (!changed) {
      return user;
    }

    return userRepositoryPort.save(user);
  }

  public UserModel internalUserProfile(Long userId) {

    UserModel user = userRepositoryPort.internalUserProfile(userId);

    if (user == null) {
      throw new NotFoundException("User with id: " + userId + " not found");
    }
    return user;
  }

  private static String trim(CharSequence cs) {
    return cs == null ? null : cs.toString().strip();
  }
}
