package com.fis.hrmservice.domain.model.ticket;

import com.fis.hrmservice.domain.model.common.BaseDomain;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LeaveRequestModel extends BaseDomain {
    private TicketModel ticket;
    private LeaveTypeModel leaveType;
    private Integer totalDays;
}