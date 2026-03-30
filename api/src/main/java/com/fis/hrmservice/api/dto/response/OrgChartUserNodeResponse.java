package com.fis.hrmservice.api.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

@Data
@Builder
public class OrgChartUserNodeResponse {

  @JsonSerialize(using = ToStringSerializer.class)
  Long id;

  String name;
  String title;
  String department;
  String avatar;
  String email;
  String phone;
  String status;
  String joinedDate;
  String location;

  @JsonSerialize(using = ToStringSerializer.class)
  Long managerId;

  boolean hasChildren;
  long subordinateCount;

  @Builder.Default
  List<OrgChartUserNodeResponse> children = new ArrayList<>();
}
