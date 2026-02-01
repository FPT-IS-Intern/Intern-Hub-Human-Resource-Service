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
import com.intern.hub.library.common.utils.Snowflake;import lombok.RequiredArgsConstructor;import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PositionRepositoryPort positionRepository;
    private final Snowflake snowflake;
    private final UserValidationService validationService;

    @Override
    public UserModel registerUser(RegisterUserCommand command) {

        validationService.validateRegistration(command);
        checkForDuplicates(command);

        PositionModel position = positionRepository
                .findByCode(command.getPositionCode())
                .orElseThrow(() -> new ConflictDataException("Position không tồn tại"));

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

        UserModel.UserModelBuilder builder = UserModel.builder()
                .userId(snowflake.next())
                .position(position)
                .fullName(command.getFullName())
                .idNumber(command.getIdNumber())
                .dateOfBirth(command.getBirthDate())
                .companyEmail(command.getEmail())
                .phoneNumber(command.getPhoneNumber())
                .address(command.getAddress())
                .sysStatus(CoreConstant.STATUS_PENDING);

        if (command.isInternRegistration()) {
            builder
                    .internshipStartDate(command.getInternshipStartDate())
                    .internshipEndDate(command.getInternshipEndDate());
        }

        return builder.build();
    }

}
