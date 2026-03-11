package com.fis.hrmservice.domain.model.constant;

import lombok.Getter;

@Getter
public enum AttendanceStatus {
  CHECK_IN_LATE("CHECK_IN_LATE"),
  CHECK_OUT_EARLY("CHECK_OUT_EARLY"),
  CHECK_OUT_ON_TIME("CHECK_OUT_ON_TIME"),
  CHECK_IN_ON_TIME("CHECK_IN_ON_TIME"),
  ABSENT("ABSENT");

  private final String value;

  AttendanceStatus(String value) {
    this.value = value;
  }
}
