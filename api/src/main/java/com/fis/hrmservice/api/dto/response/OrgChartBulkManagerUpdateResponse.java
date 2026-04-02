package com.fis.hrmservice.api.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrgChartBulkManagerUpdateResponse {
  List<Long> updatedUserIds;
  Long managerId;
  int updatedCount;
}
