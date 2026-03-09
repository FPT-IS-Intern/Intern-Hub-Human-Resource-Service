package com.fis.hrmservice.domain.model.resonse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRoleCoreResponse {
    String id;
    String name;
    String description;
    String status;
}
