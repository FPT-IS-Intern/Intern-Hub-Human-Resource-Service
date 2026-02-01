package com.fis.hrmservice.domain.model.ticket;

import com.fis.hrmservice.domain.model.user.WorkLocationModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RemoteRequestModel {
    private TicketModel ticket;
    private WorkLocationModel workLocation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}