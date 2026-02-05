package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.input.user.RegisterUserUseCase;
import com.fis.hrmservice.domain.port.output.user.PositionRepositoryPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.fis.hrmservice.domain.service.UserValidationService;
import com.fis.hrmservice.domain.usecase.command.user.RegisterUserCommand;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.utils.Snowflake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegisterUserUseCaseImpl {

  @Autowired
  private UserRepositoryPort userRepository;
  @Autowired
  private PositionRepositoryPort positionRepository;
  @Autowired
  private Snowflake snowflake;
  @Autowired
  private UserValidationService validationService;

  public UserModel registerUser(RegisterUserCommand command) {

    validationService.validateRegistration(command);
    checkForDuplicates(command);
    PositionModel position =
        positionRepository
            .findByCode(command.getPositionCode())
            .orElseThrow(() -> new ConflictDataException("Position không tồn tại"));
    log.debug("Position found: {}", position);
    UserModel user = buildUserModel(command, position);

    return userRepository.save(user);
  }

  private void checkForDuplicates(RegisterUserCommand command) {
    if (userRepository.existsByEmail(command.getEmail())) {
      throw new ConflictDataException("Email đã tồn tại");
    }

    if (userRepository.existsByIdNumber(command.getIdNumber())) {
      throw new ConflictDataException("Id user đã bị trùng");
    }
  }

  private UserModel buildUserModel(RegisterUserCommand command, PositionModel position) {

    UserModel.UserModelBuilder builder =
        UserModel.builder()
            .userId(snowflake.next())
            .position(position)
            .fullName(command.getFullName())
            .idNumber(command.getIdNumber())
            .dateOfBirth(command.getBirthDate())
            .companyEmail(command.getEmail())
            .phoneNumber(command.getPhoneNumber())
            .address(command.getAddress())
            .sysStatus(UserStatus.PENDING);

    if (command.isInternRegistration()) {
      builder
          .internshipStartDate(command.getInternshipStartDate())
          .internshipEndDate(command.getInternshipEndDate());
    }

    return builder.build();
  }
}
