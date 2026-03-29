package com.fis.hrmservice.domain.port.output.feign;

import com.fis.hrmservice.domain.model.dto.resonse.SidebarMenuCoreResponse;

import java.util.List;

public interface SidebarMenuPort {
  List<SidebarMenuCoreResponse> getSidebarMenusByRoles(List<String> roles);
}
