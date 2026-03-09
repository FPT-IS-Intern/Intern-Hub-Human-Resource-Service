package com.fis.hrmservice.api.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SupervisorMemberResponse {
    int no;
    String avatarUrl;
    String fullName;
    String sysStatus;
    String companyEmail;
    String role;
    String position;
}
