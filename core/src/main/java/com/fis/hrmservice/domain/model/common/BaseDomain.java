package com.fis.hrmservice.domain.model.common;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class BaseDomain {
  long createdAt;
  long updatedAt;
  Integer version;
  String status;
  String createdBy;
  String updatedBy;
}
