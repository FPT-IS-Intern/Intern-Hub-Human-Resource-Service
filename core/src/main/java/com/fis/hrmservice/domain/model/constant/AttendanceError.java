package com.fis.hrmservice.domain.model.constant;

import lombok.Getter;

@Getter
public enum AttendanceError {
  ALREADY_CHECKED_IN("ALREADY_CHECKED_IN"),
  OPEN_SESSION_EXISTS("OPEN_SESSION_EXISTS"),
  OPEN_SESSION_NOT_FOUND("OPEN_SESSION_NOT_FOUND"),
  OUT_OF_RANGE("OUT_OF_RANGE");

  private final String value;

  AttendanceError(String value) {
    this.value = value;
  }
}
