package com.fis.hrmservice.api.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class WiFiInfoResponse {
  private String wifiName;
  private boolean isCompanyWifi;
  private UUID branchId;
}
