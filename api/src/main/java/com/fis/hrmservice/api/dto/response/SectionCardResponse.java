package com.fis.hrmservice.api.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SectionCardResponse {
    int totalWorkDate;
    int totalLateDate;
    int absentWithLicense;
    int absentWithoutLicense;
}
