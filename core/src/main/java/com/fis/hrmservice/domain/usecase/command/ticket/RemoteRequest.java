package com.fis.hrmservice.domain.usecase.command.ticket;

import com.fis.hrmservice.domain.model.constant.RemoteType;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoteRequest {
  LocalTime startTime;
  LocalTime endTime;
  RemoteType remoteType;
  String location;
}
