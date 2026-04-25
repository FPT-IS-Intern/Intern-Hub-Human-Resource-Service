package com.fis.hrmservice.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SidebarMenuResponse {
  private Integer id;
  private String code;
  private String title;
  private String path;
  private String icon;
  private Integer parentId;
  private Integer sortOrder;
  private String status;
  private List<SidebarMenuResponse> children;
}
