package com.fis.hrmservice.infra.feign.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListRoleInfraResponse {
    String id;
    String name;
    String description;
    String status;
}
