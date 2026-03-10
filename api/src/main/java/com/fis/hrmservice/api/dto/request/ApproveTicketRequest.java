package com.fis.hrmservice.api.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ApproveTicketRequest {
    private String roleId;
}
