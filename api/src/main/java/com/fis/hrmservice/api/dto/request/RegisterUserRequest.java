package com.fis.hrmservice.api.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterUserRequest {

  @NotBlank(message = "Email không được để trống")
  @Email(message = "Email không đúng định dạng")
  String email;

  @NotBlank(message = "Họ tên không được để trống")
  @Size(max = 100, message = "Họ tên không được quá 100 ký tự")
  String fullName;

  @NotBlank(message = "Số CCCD/CMND không được để trống")
  @Size(min = 12, max = 12, message = "Số CCCD phải có 12 ký tự")
  String idNumber;

  @NotNull(message = "Ngày sinh không được để trống")
  @Past(message = "Ngày sinh phải là ngày trong quá khứ")
  LocalDate birthDate;

  @NotBlank(message = "Địa chỉ không được để trống")
  String address;

  @NotBlank(message = "Số điện thoại không được để trống")
  @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại không đúng định dạng")
  String phoneNumber;

  @NotBlank(message = "Vị trí không được để trống")
  String positionCode;

  // Intern specific fields (optional for non-intern positions)
  LocalDate internshipStartDate;
  LocalDate internshipEndDate;

  // File uploads
  //    @NotNull(message = "CV không được để trống")
  MultipartFile cv;

  //    @NotNull(message = "Ảnh đại diện không được để trống")
  MultipartFile avatar;
}
