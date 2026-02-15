package com.fis.hrmservice.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WiFiInfoResponse {
  private String wifiName;
  private boolean isCompanyWifi;
}
