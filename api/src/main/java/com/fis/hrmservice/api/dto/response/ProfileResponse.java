package com.fis.hrmservice.api.dto.response;

import java.time.LocalDate;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileResponse {
  private String fullName;
  private String idNumber;
  private LocalDate dateOfBirth;
  private String phoneNumber;
  private String companyEmail;
  private String address;
  private String position;
  private LocalDate internshipStartDate;
  private LocalDate internshipEndDate;
  private String avatarUrl;
  private String cvUrl;
  private String role;
  private String superVisorName;
  private int scoreAward;
  private int budgetPoints;
  private String sysStatus;
}
