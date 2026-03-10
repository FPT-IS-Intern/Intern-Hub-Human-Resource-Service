package com.fis.hrmservice.infra.feign.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AssignRoleRequest {
    private Long roleId;
}