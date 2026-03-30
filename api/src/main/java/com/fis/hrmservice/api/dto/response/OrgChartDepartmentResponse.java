package com.fis.hrmservice.api.dto.response;

import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

@Data
@Builder
public class OrgChartDepartmentResponse {

  @JsonSerialize(using = ToStringSerializer.class)
  Long id;

  String name;
  String code;
}
