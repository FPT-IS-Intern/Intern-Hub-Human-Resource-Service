package com.fis.hrmservice.infra.feign.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRoleInfraResponse {
    String id;
    String name;
    String description;
    String status;
}
