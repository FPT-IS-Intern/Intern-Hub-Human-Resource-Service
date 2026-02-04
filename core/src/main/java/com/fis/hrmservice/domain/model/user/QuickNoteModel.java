package com.fis.hrmservice.domain.model.user;

import com.fis.hrmservice.domain.model.common.BaseDomain;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class QuickNoteModel extends BaseDomain {

  private Long id;
  private UserModel intern;
  private UserModel writer;

  private String content;
  private LocalDateTime writeDate;
}
