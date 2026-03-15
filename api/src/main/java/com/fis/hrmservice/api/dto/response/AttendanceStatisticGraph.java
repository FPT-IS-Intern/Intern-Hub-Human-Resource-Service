package com.fis.hrmservice.api.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceStatisticGraph {
    Long onTimePercentage;
    Long latePercentage;
    Long absentPercentage;
}
