package com.fis.hrmservice.api.dto.request;

import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterRequest {
  String keyword;
  List<String> sysStatuses;
  List<String> roles;
  List<String> positions;
}
