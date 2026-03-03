package com.fis.hrmservice.domain.usecase.command.attendance;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CheckOutCommand {
  private Long userId;
  private long checkOutTime;
  private String clientIp;
  private Double latitude;
  private Double longitude;
}
