package com.fis.hrmservice.domain.service;

import com.fis.hrmservice.domain.model.constant.CoreConstant;
import com.fis.hrmservice.domain.usecase.command.user.RegisterUserCommand;
import com.fis.hrmservice.domain.usecase.command.user.UpdateUserProfileCommand;
import com.intern.hub.library.common.exception.ConflictDataException;
import java.time.LocalDate;
import java.time.ZoneId;
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

    // TODO: cái này từ từ tính sau
    //        if (idNumber == null || idNumber.isBlank()) {
    //            throw new ConflictDataException("Số CCCD/CMND không được để trống");
    //        }
    //
    //        if (!ID_NUMBER_PATTERN.matcher(idNumber).matches()) {
    //            throw new ConflictDataException("Sai định dạng CCCD/CMND");
    //        }
    //
    //        // Validate province code (first 3 digits)
    //        Integer provinceCode = ProvinceCode.getProvinceCode(address);
    //        if (provinceCode == null) {
    //            throw new ConflictDataException("address", "Không tìm thấy mã tỉnh/thành phố từ
    // địa chỉ");
    //        }
    //
    //        String firstThreeDigits = idNumber.substring(0, 3);
    //        String expectedProvinceCode = String.format("%03d", provinceCode);
    //        if (!firstThreeDigits.equals(expectedProvinceCode)) {
    //            throw new ConflictDataException("Mã tỉnh/thành phố trên CCCD không khớp với địa
    // chỉ");
    //        }

    if (idNumber == null || idNumber.isBlank()) {
      throw new ConflictDataException("Số CCCD/CMND không được để trống");
    }

    if (idNumber.length() != 12) {
      throw new ConflictDataException("Số CCCD/CMND không hợp lệ");
    }

    // Validate century and gender code (4th digit)
    validateCenturyCode(idNumber, dateOfBirth);
  }

  private void validateCenturyCode(String idNumber, LocalDate dateOfBirth) {
    if (dateOfBirth == null) {
      throw new ConflictDataException("Ngày sinh không được để trống");
    }

    int sexCode = Character.getNumericValue(idNumber.charAt(3));
    int birthYear = dateOfBirth.getYear();
    int century = (birthYear - 1) / 100 + 1;

    // Century code validation based on Vietnamese ID card rules
    boolean validCentury =
        switch (century) {
          case 20 -> sexCode == 0 || sexCode == 1; // 1900-1999: 0 (male), 1 (female)
          case 21 -> sexCode == 2 || sexCode == 3; // 2000-2099: 2 (male), 3 (female)
          case 22 -> sexCode == 4 || sexCode == 5; // 2100-2199: 4 (male), 5 (female)
          default -> false;
        };

    if (!validCentury) {
      throw new ConflictDataException("Mã thế kỷ trên CCCD không khớp với năm sinh");
    }
  }

  public void validateFileMetadata(RegisterUserCommand command) {

    // Validate CV
    validateCvFile(command.getCvContentType(), command.getCvSize());

    // Validate Avatar
    validateAvatarFile(command.getAvatarContentType(), command.getAvatarSize());
  }

  public void validateFileMetadataUpdate(UpdateUserProfileCommand command) {

    if (command.getCvFile() != null) {
      validateCvFile(command.getCvFile().getContentType(), command.getCvFile().getSize());
    }

    if (command.getAvatarFile() != null) {
      validateAvatarFile(command.getAvatarFile().getContentType(), command.getAvatarFile().getSize());
    }


    // Validate CV
    validateCvFile(command.getCvFile().getContentType(), command.getCvFile().getSize());

    // Validate Avatar
    validateAvatarFile(command.getAvatarFile().getContentType(), command.getAvatarFile().getSize());
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

    boolean validType =
        CoreConstant.MIME_TYPE_PNG.equals(contentType)
            || CoreConstant.MIME_TYPE_JPG.equals(contentType);

    if (!validType) {
      throw new ConflictDataException("Ảnh đại diện phải có định dạng PNG hoặc JPG");
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

    LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));

    if (startDate.isBefore(today)) {
      throw new ConflictDataException("Ngày bắt đầu thực tập không được trước ngày hiện tại");
    }

    if (!endDate.isAfter(startDate)) {
      throw new ConflictDataException("Ngày kết thúc phải sau ngày bắt đầu thực tập");
    }
  }

}
