package com.fis.hrmservice.api.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

@Data
@Builder
public class OrgChartUserDetailResponse {

  @JsonSerialize(using = ToStringSerializer.class)
  Long id;

  String name;
  String title;
  OrgChartDepartmentResponse department;
  String avatar;
  String email;
  String phone;
  String status;
  String joinedDate;
  String location;
  OrgChartUserLiteResponse manager;
  List<OrgChartUserLiteResponse> subordinates;
  List<String> projects;
  boolean hasChildren;
  long subordinateCount;
}
