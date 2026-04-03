package com.fis.hrmservice.api.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceStatisticGraph {
    Double onTimePercentage;
    Double latePercentage;
    Double absentPercentage;
}
