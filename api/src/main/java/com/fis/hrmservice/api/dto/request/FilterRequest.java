package com.fis.hrmservice.api.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterRequest {
    String keyword;
    String sysStatus;
    String position;
    String role;
}
