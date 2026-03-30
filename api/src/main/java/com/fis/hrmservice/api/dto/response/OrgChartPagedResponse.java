package com.fis.hrmservice.api.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrgChartPagedResponse<T> {
  List<T> data;
  OrgChartPageMetaResponse meta;
}
