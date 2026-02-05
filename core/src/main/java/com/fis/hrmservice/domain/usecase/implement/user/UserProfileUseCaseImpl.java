package com.fis.hrmservice.domain.usecase.implement.user;

import static cn.hutool.core.text.CharSequenceUtil.trim;

import com.fis.hrmservice.common.utils.UpdateHelper;
import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.fis.hrmservice.domain.service.UserValidationService;
import com.fis.hrmservice.domain.usecase.command.user.UpdateUserProfileCommand;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileUseCaseImpl {

  @Autowired
  private UserRepositoryPort userRepositoryPort;

  @Autowired
  private UserValidationService userValidationService;


  public UserModel getUserProfile(Long userId) {

    return userRepositoryPort
        .findById(userId)
        .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
  }

  public UserModel updateProfileUser(UpdateUserProfileCommand command, long userId) {

    /*
       TODO: khi nào api gateway làm xong mới dùng được
    */

    //        AuthContext context = AuthContextHolder.get()
    //                .orElseThrow(() -> new NotFoundException("Not authenticated"));
    //
    //        long userId = context.userId();

    UserModel userModel =
        userRepositoryPort
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

    userValidationService.validateUpdate(command);

    boolean changed = false;

    changed |=
        UpdateHelper.applyIfChanged(
            command.getFullName(),
            userModel::getFullName,
            userModel::setFullName,
            (oldVal, newVal) -> Objects.equals(trim(oldVal), trim(newVal)));

    changed |=
        UpdateHelper.applyIfChanged(
            command.getCompanyEmail(),
            userModel::getCompanyEmail,
            userModel::setCompanyEmail,
            (oldVal, newVal) -> Objects.equals(trim(oldVal), trim(newVal)));

    changed |=
        UpdateHelper.applyIfChanged(
            command.getDateOfBirth(),
            userModel::getDateOfBirth,
            userModel::setDateOfBirth,
            Objects::equals);

    changed |=
        UpdateHelper.applyIfChanged(
            command.getIdNumber(),
            userModel::getIdNumber,
            userModel::setIdNumber,
            (oldVal, newVal) -> Objects.equals(trim(oldVal), trim(newVal)));

    changed |=
        UpdateHelper.applyIfChanged(
            command.getAddress(),
            userModel::getAddress,
            userModel::setAddress,
            (oldVal, newVal) -> Objects.equals(trim(oldVal), trim(newVal)));

    changed |=
        UpdateHelper.applyIfChanged(
            command.getPhoneNumber(),
            userModel::getPhoneNumber,
            userModel::setPhoneNumber,
            (oldVal, newVal) -> Objects.equals(trim(oldVal), trim(newVal)));

    /*
       TODO: upload file làm sau
    */

    //        if (command.getCvFile() != null && !command.getCvFile().isEmpty()) {
    //
    //            FileUploadResult result = fileStoragePort.upload(command.getCvFile());
    //
    //            CvModel cv = CvModel.builder()
    //                    .user(userModel)
    //                    .cvUrl(result.url())
    //                    .fileName(result.fileName())
    //                    .fileSize(result.fileSize())
    //                    .fileType(result.contentType())
    //                    .status("ACTIVE")
    //                    .build();
    //
    //            cvRepositoryPort.save(cv);
    //            changed = true;
    //        }
    //
    //        if (command.getAvatarFile() != null && !command.getAvatarFile().isEmpty()) {
    //
    //            FileUploadResult result = fileStoragePort.upload(command.getAvatarFile());
    //
    //            AvatarModel avatar = AvatarModel.builder()
    //                    .user(userModel)
    //                    .avatarUrl(result.url())
    //                    .fileName(result.fileName())
    //                    .fileSize(result.fileSize())
    //                    .fileType(result.contentType())
    //                    .status("ACTIVE")
    //                    .build();
    //
    //            avatarRepositoryPort.save(avatar);
    //            userModel.setAvatarUrl(result.url());
    //            changed = true;
    //        }

    if (!changed) {
      throw new ConflictDataException("No changes detected in the profile update request");
    }

    PositionModel oldPosition = userModel.getPosition();

    userModel.setPosition(oldPosition);

    return userRepositoryPort.save(userModel);
  }
}
