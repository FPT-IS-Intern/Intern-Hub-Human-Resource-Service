package com.fis.hrmservice.infra.feign.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SetUserRoleResponse {
    String field;
    String message;
}
