package com.fis.hrmservice.infra.feign.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleInfraResponse {
    String id;
    String name;
    String description;
    String status;
}
