package com.fis.hrmservice.domain.usecase.command.user;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserProfileCommand {
  String fullName;
  String companyEmail;
  LocalDate dateOfBirth;
  String idNumber;
  String address;
  String phoneNumber;
  MultipartFile cvFile;
  MultipartFile avatarFile;
}
