package com.fis.hrmservice.domain.model.dto.resonse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SetUserRoleCoreResponse {
    String field;
    String message;
}
