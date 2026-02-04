package com.fis.hrmservice.common.utils;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateValidationHelper {

  public static void validateDate(LocalDate fromDate, LocalDate toDate) {
    if (fromDate == null || toDate == null) {
      throw new IllegalArgumentException("You must provide from date and to date");
    }

    if (fromDate.isAfter(toDate)) {
      throw new IllegalArgumentException("From date must be before to date");
    }
  }

  public static void validateHour(LocalTime stratAt, LocalTime endAt) {
    if (stratAt == null || endAt == null) {
      throw new IllegalArgumentException("You must provide time");
    }

    if (stratAt.isAfter(endAt)) {
      throw new IllegalArgumentException("Start time must be before end time");
    }
  }
}
