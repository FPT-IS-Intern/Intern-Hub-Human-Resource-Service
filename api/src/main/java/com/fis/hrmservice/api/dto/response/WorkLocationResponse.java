package com.fis.hrmservice.api.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WorkLocationResponse {
    Long workLocationId;
    String name;
    String address;
    String description;
    Boolean isActive;
}
