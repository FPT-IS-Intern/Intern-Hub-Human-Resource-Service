package com.fis.hrmservice.api.dto.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuickNoteResponse {
  LocalDateTime createDate;
  String content;
}
