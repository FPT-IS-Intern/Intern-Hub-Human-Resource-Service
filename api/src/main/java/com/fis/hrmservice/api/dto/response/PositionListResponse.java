package com.fis.hrmservice.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionListResponse {
  @JsonSerialize(using = ToStringSerializer.class)
  Long positionId;
  String name;
}
