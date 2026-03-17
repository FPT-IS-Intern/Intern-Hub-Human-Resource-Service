package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.constant.CoreConstant;
import com.fis.hrmservice.domain.model.constant.TicketStatus;
import com.fis.hrmservice.domain.model.constant.TicketType;
import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.ticket.TicketModel;
import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.ticket.TicketRepositoryPort;
import com.fis.hrmservice.domain.port.output.ticket.TicketTypeRepositoryPort;
import com.fis.hrmservice.domain.port.output.user.*;
import com.fis.hrmservice.domain.service.UserValidationService;
import com.fis.hrmservice.domain.usecase.command.user.RegisterUserCommand;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.utils.Snowflake;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
public class RegisterUserUseCaseImpl {

    UserRepositoryPort userRepositoryPort;
    PositionRepositoryPort positionRepositoryPort;
    Snowflake snowflake;
    UserValidationService validationService;
    TicketRepositoryPort ticketRepositoryPort;
    TicketTypeRepositoryPort ticketTypeRepositoryPort;
    FileStoragePort fileStoragePort;

    @Transactional(rollbackFor = Exception.class)
    public UserModel registerUser(RegisterUserCommand command) {

        //Validate business rule
        validationService.validateRegistration(command);
        checkForDuplicates(command);
        validateFiles(command);
        isOver18(command.getBirthDate());

        //Get position
        PositionModel position =
                positionRepositoryPort
                        .findByCode(command.getPositionCode())
                        .orElseThrow(() -> new ConflictDataException("Position không tồn tại"));

        Long stagedUserId = snowflake.next();

        try {

            //Upload Avatar via DMS
            String avatarObjectKey =
                    fileStoragePort.uploadFile(
                            command.getAvatar(),
                            "avatars/" + command.getAvatar().getOriginalFilename(),
                            stagedUserId,
                            20971520L,
                            "image/(png|jpeg|jpg)");

            //Upload CV via DMS
            String cvObjectKey = fileStoragePort.uploadFile(
                    command.getCv(),
                    "cvs/" + command.getCv().getOriginalFilename(),
                    stagedUserId,
                    20971520L,
                    "application/pdf|application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            );

            Map<String, Object> userInfoTemp =
                    buildUserInfoTemp(command, position, stagedUserId, CoreConstant.S3_PREFIX_URL + avatarObjectKey, CoreConstant.S3_PREFIX_URL + cvObjectKey);

            //Create registration ticket with staged profile JSON
            ticketRepositoryPort.save(
                    TicketModel.builder()
                            .ticketId(snowflake.next())
                            .ticketType(
                                    ticketTypeRepositoryPort.findTicketTypeByCode(
                                            String.valueOf(TicketType.REGISTRATION)))
                            .startAt(LocalDate.now())
                            .endAt(null)
                            .reason("Đăng ký tài khoản")
                            .userInfoTemp(userInfoTemp)
                            .sysStatus(TicketStatus.PENDING)
                            .build());

            return buildStagedUserModel(command, position, stagedUserId, CoreConstant.S3_PREFIX_URL + avatarObjectKey, CoreConstant.S3_PREFIX_URL + cvObjectKey);

        } catch (Exception e) {
            log.error("Register process failed. Transaction rollback triggered.", e);
            throw new ConflictDataException("không thể up avatar và cv lên DMS: " + e.getMessage());
        }
    }

    private void validateFiles(RegisterUserCommand command) {

        // ===== AVATAR =====
        if (command.getAvatar() == null || command.getAvatar().isEmpty()) {
            throw new ConflictDataException("Avatar is required");
        }

        // Check size > 2MB
        if (command.getAvatar().getSize() > 2 * 1024 * 1024) {
            throw new ConflictDataException("Avatar vượt quá 2MB");
        }

        String avatarType = command.getAvatar().getContentType();
        if (avatarType == null || !avatarType.matches("image/(png|jpeg|webp)")) {
            throw new ConflictDataException("Unsupported avatar type");
        }

        // ===== CV =====
        if (command.getCv() == null || command.getCv().isEmpty()) {
            throw new ConflictDataException("CV is required");
        }

        // Check size > 10MB
        if (command.getCv().getSize() > 10 * 1024 * 1024) {
            throw new ConflictDataException("CV vượt quá 10MB");
        }

        String cvType = command.getCv().getContentType();
        if (cvType == null
                || !(cvType.equals("application/pdf")
                || cvType.equals(
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
            throw new ConflictDataException("CV must be PDF or DOCX");
        }
    }

    private void checkForDuplicates(RegisterUserCommand command) {

        if (userRepositoryPort.existsByEmail(command.getEmail())) {
            throw new ConflictDataException("Tài khoản đã được đăng ký");
        }

        if (userRepositoryPort.existsByIdNumber(command.getIdNumber())) {
            throw new ConflictDataException("Số CCCD đã được dùng để đăng ký");
        }
    }

    private UserModel buildStagedUserModel(
            RegisterUserCommand command,
            PositionModel position,
            Long stagedUserId,
            String avatarUrl,
            String cvUrl) {

        UserModel.UserModelBuilder builder =
                UserModel.builder()
                        .userId(stagedUserId)
                        .position(position)
                        .fullName(command.getFullName())
                        .idNumber(command.getIdNumber())
                        .dateOfBirth(command.getBirthDate())
                        .companyEmail(command.getEmail().toLowerCase())
                        .phoneNumber(command.getPhoneNumber())
                        .address(command.getAddress())
                        .avatarUrl(avatarUrl)
                        .cvUrl(cvUrl)
                        .sysStatus(UserStatus.PENDING);

        if (command.isInternRegistration()) {
            builder
                    .internshipStartDate(command.getInternshipStartDate())
                    .internshipEndDate(command.getInternshipEndDate());
        }

        return builder.build();
    }

    private Map<String, Object> buildUserInfoTemp(
            RegisterUserCommand command,
            PositionModel position,
            Long stagedUserId,
            String avatarUrl,
            String cvUrl) {
        Map<String, Object> temp = new HashMap<>();
        temp.put("userId", stagedUserId);
        temp.put("positionId", position.getPositionId());
        temp.put("positionCode", command.getPositionCode());
        temp.put("fullName", command.getFullName());
        temp.put("idNumber", command.getIdNumber());
        temp.put("dateOfBirth", command.getBirthDate() != null ? command.getBirthDate().toString() : null);
        temp.put("companyEmail", command.getEmail() != null ? command.getEmail().toLowerCase() : null);
        temp.put("phoneNumber", command.getPhoneNumber());
        temp.put("address", command.getAddress());
        temp.put(
                "internshipStartDate",
                command.getInternshipStartDate() != null ? command.getInternshipStartDate().toString() : null);
        temp.put(
                "internshipEndDate",
                command.getInternshipEndDate() != null ? command.getInternshipEndDate().toString() : null);
        temp.put("avatarUrl", avatarUrl);
        temp.put("cvUrl", cvUrl);
        return temp;
    }

    private void isOver18(LocalDate dateOfBirth) {
        int thisYear = LocalDate.now().getYear();

        if (thisYear - dateOfBirth.getYear() < 16) {
            throw new ConflictDataException("Ứng viên phải trên 16 tuổi");
        }
    }
}
