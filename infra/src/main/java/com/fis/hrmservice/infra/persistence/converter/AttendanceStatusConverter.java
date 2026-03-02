package com.fis.hrmservice.infra.persistence.converter;

import com.fis.hrmservice.domain.model.constant.AttendanceStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class AttendanceStatusConverter implements AttributeConverter<AttendanceStatus, String> {

  @Override
  public String convertToDatabaseColumn(AttendanceStatus attribute) {
    if (attribute == null) {
      return null;
    }
    return attribute.name();
  }

  @Override
  public AttendanceStatus convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isBlank()) {
      return null;
    }
    try {
      return AttendanceStatus.valueOf(dbData);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}

