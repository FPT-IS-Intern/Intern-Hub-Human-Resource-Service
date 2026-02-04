package com.fis.hrmservice.api.dto.request;

import java.time.LocalDate;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
  String fullName;
  String companyEmail;
  LocalDate dateOfBirth;
  String idNumber;
  String address;
  String phoneNumber;
  MultipartFile cvFile;
  MultipartFile avatarFile;
}
