package com.fis.hrmservice.domain.service;

import com.fis.hrmservice.domain.model.constant.CoreConstant;
import com.fis.hrmservice.domain.usecase.command.user.RegisterUserCommand;
import com.fis.hrmservice.domain.usecase.command.user.UpdateUserProfileCommand;
import com.intern.hub.library.common.exception.ConflictDataException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class UserValidationService {

  private static final Pattern EMAIL_PATTERN = Pattern.compile(CoreConstant.EMAIL_FORMAT);
  private static final Pattern ID_NUMBER_PATTERN = Pattern.compile(CoreConstant.ID_NUMBER_FORMAT);
  private static final Pattern PHONE_PATTERN = Pattern.compile(CoreConstant.PHONE_NUMBER_FORMAT);

  public void validateRegistration(RegisterUserCommand command) {
    validateEmail(command.getEmail());
    validatePhoneNumber(command.getPhoneNumber());
    validateIdNumber(command.getIdNumber(), command.getBirthDate(), command.getAddress());
    validateFileMetadata(command);

    if (command.isInternRegistration()) {
      validateInternshipDates(command.getInternshipStartDate(), command.getInternshipEndDate());
    }
  }

  public void validateUpdate(UpdateUserProfileCommand command) {
    validateEmail(command.getCompanyEmail());
    validatePhoneNumber(command.getPhoneNumber());
    validateIdNumber(command.getIdNumber(), command.getDateOfBirth(), command.getAddress());
    validateFileMetadataUpdate(command);
  }

  public void validateEmail(String email) {
    if (email == null || email.isBlank()) {
      throw new ConflictDataException("Email không được để trống");
    }
    if (!EMAIL_PATTERN.matcher(email).matches()) {
      throw new ConflictDataException("Sai định dạng email. Email phải có dạng xxx@fpt.com");
    }
  }

  public void validatePhoneNumber(String phoneNumber) {
    if (phoneNumber == null || phoneNumber.isBlank()) {
      throw new ConflictDataException("Số điện thoại không được để trống");
    }
    if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
      throw new ConflictDataException("Sai định dạng số điện thoại");
    }
  }

  public void validateIdNumber(String idNumber, LocalDate dateOfBirth, String address) {
    if (idNumber == null || idNumber.isBlank()) {
      throw new ConflictDataException("Số CCCD/CMND không được để trống");
    }

    if (idNumber.length() != 12 && idNumber.length() != 9) {
      throw new ConflictDataException("Số CCCD/CMND không hợp lệ");
    }

    if (idNumber.length() == 12) {
      validateNationalId12(idNumber, dateOfBirth);
    }
  }

  private void validateNationalId12(String idNumber, LocalDate dateOfBirth) {
    if (dateOfBirth == null) {
      throw new ConflictDataException("Ngày sinh không được để trống");
    }

    int sexCode = Character.getNumericValue(idNumber.charAt(3));
    int birthYear = dateOfBirth.getYear();
    int centuryBlock = birthYear / 100 + 1;

    boolean validCentury =
        switch (centuryBlock) {
          case 20 -> sexCode == 0 || sexCode == 1; // 1900-1999: 0 (male), 1 (female)
          case 21 -> sexCode == 2 || sexCode == 3; // 2000-2099: 2 (male), 3 (female)
          case 22 -> sexCode == 4 || sexCode == 5; // 2100-2199: 4 (male), 5 (female)
          default -> false;
        };

    if (!validCentury) {
      throw new ConflictDataException("Mã thế kỷ trên CCCD không khớp với năm sinh");
    }

    String birthYearCode = idNumber.substring(4, 6);
    int birthYearCodeNumber = Integer.parseInt(birthYearCode);
    boolean validBirthYear = (dateOfBirth.getYear() % 100) == birthYearCodeNumber;

    if (!validBirthYear) {
      throw new ConflictDataException("Mã năm sinh trên CCCD không khớp với năm sinh");
    }
  }


  public void validateFileMetadata(RegisterUserCommand command) {

    // Validate CV
    if (command.getCv() == null || command.getCv().isEmpty()) {
      throw new ConflictDataException("File CV không được để trống");
    }
    validateCvFile(command.getCv().getContentType(), command.getCv().getSize());

    // Validate Avatar
    if (command.getAvatar() == null || command.getAvatar().isEmpty()) {
      throw new ConflictDataException("File ảnh đại diện không được để trống");
    }
    validateAvatarFile(command.getAvatar().getContentType(), command.getAvatar().getSize());
  }

  public void validateFileMetadataUpdate(UpdateUserProfileCommand command) {

    if (command.getCvFile() != null) {
      validateCvFile(command.getCvFile().getContentType(), command.getCvFile().getSize());
    }

    if (command.getAvatarFile() != null) {
      validateAvatarFile(
          command.getAvatarFile().getContentType(), command.getAvatarFile().getSize());
    }
  }

  public void validateCvFile(String contentType, long size) {
    if (contentType == null) {
      throw new ConflictDataException("File CV không được để trống");
    }

    boolean validType =
        CoreConstant.MIME_TYPE_PDF.equals(contentType)
            || CoreConstant.MIME_TYPE_DOCX.equals(contentType);

    if (!validType) {
      throw new ConflictDataException("File CV phải có định dạng PDF hoặc DOCX");
    }

    if (size > CoreConstant.MAX_FILE_SIZE_BYTES) {
      throw new ConflictDataException(
          "cv", String.format("File CV không được vượt quá %dMB", CoreConstant.MAX_FILE_SIZE_MB));
    }
  }

  public void validateAvatarFile(String contentType, long size) {
    if (contentType == null) {
      throw new ConflictDataException("File ảnh đại diện không được để trống");
    }

    boolean validType = contentType.startsWith("image/");

    if (!validType) {
      throw new ConflictDataException("Ảnh đại diện sai định dạng");
    }

    if (size > CoreConstant.MAX_FILE_SIZE_BYTES) {
      throw new ConflictDataException(
          String.format("Ảnh đại diện không được vượt quá %dMB", CoreConstant.MAX_FILE_SIZE_MB));
    }
  }

  public void validateInternshipDates(LocalDate startDate, LocalDate endDate) {
    if (startDate == null) {
      throw new ConflictDataException("Ngày bắt đầu thực tập không được để trống");
    }

    if (endDate == null) {
      throw new ConflictDataException("Ngày kết thúc thực tập không được để trống");
    }

    if (!endDate.isAfter(startDate)) {
      throw new ConflictDataException("Ngày kết thúc phải sau ngày bắt đầu thực tập");
    }
  }
}
