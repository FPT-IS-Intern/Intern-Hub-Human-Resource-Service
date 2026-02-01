package com.fis.hrmservice.domain.model.ticket;

import com.fis.hrmservice.domain.model.common.BaseDomain;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TicketTypeModel extends BaseDomain {
    private Long ticketTypeId;
    private String typeName;
    private String description;
}