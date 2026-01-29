package com.fis.hrmservice.domain.usecase.command;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegisterUserCommand {

    String email;
    String fullName;
    String idNumber;
    LocalDate birthDate;
    String address;
    String phoneNumber;
    String positionCode;

    LocalDate internshipStartDate;
    LocalDate internshipEndDate;

    String cvFileName;
    String cvContentType;
    long cvSize;

    String avatarFileName;
    String avatarContentType;
    long avatarSize;

    public boolean isInternRegistration() {
        return positionCode != null && positionCode.toUpperCase().contains("INTERN");
    }
}
