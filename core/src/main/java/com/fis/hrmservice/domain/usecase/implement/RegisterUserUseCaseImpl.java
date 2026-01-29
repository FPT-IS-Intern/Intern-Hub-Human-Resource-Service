package com.fis.hrmservice.domain.usecase.implement;

import com.fis.hrmservice.domain.model.constant.CoreConstant;
import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.input.RegisterUserUseCase;
import com.fis.hrmservice.domain.port.output.PositionRepositoryPort;
import com.fis.hrmservice.domain.port.output.UserRepositoryPort;
import com.fis.hrmservice.domain.service.UserValidationService;
import com.fis.hrmservice.domain.usecase.command.RegisterUserCommand;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.utils.Snowflake;

import java.util.Optional;

public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PositionRepositoryPort positionRepository;
    private final Snowflake snowflake;
    private final UserValidationService validationService;

    public RegisterUserUseCaseImpl(
            UserRepositoryPort userRepository,
            PositionRepositoryPort positionRepository,
            Snowflake snowflake,
            UserValidationService validationService) {
        this.userRepository = userRepository;
        this.positionRepository = positionRepository;
        this.snowflake = snowflake;
        this.validationService = validationService;
    }

    @Override
    public UserModel registerUser(RegisterUserCommand command) {
        // 1. Validate input
        validationService.validateRegistration(command);

        // 2. Check for duplicates
        checkForDuplicates(command);

        // 3. Find position
        Optional<PositionModel> positionModel = positionRepository.findByCode(command.getPositionCode());

        // 4. Build user model
        UserModel user = buildUserModel(command, positionModel.get().getPositionId());

        // 5. Save and return
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

    private UserModel buildUserModel(RegisterUserCommand command, Long positionId) {
        UserModel.UserModelBuilder builder = UserModel.builder()
                .userId(snowflake.next())
                .positionId(positionId)
                .fullName(command.getFullName())
                .idNumber(command.getIdNumber())
                .dateOfBirth(command.getBirthDate())
                .companyEmail(command.getEmail())
                .phoneNumber(command.getPhoneNumber())
                .address(command.getAddress())
                .status(CoreConstant.STATUS_PENDING);

        if (command.isInternRegistration()) {
            builder.internshipStartDate(command.getInternshipStartDate())
                   .internshipEndDate(command.getInternshipEndDate());
        }

        return builder.build();
    }
}
