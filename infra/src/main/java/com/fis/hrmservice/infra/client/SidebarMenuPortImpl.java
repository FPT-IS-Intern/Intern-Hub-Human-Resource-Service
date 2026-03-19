package com.fis.hrmservice.infra.client;

import com.fis.hrmservice.domain.model.resonse.SidebarMenuCoreResponse;
import com.fis.hrmservice.domain.port.output.feign.SidebarMenuPort;
import com.fis.hrmservice.infra.feign.client.BoPortalFeignClient;
import com.fis.hrmservice.infra.model.BoPortalSidebarMenuRequest;
import com.fis.hrmservice.infra.model.BoPortalSidebarMenuResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SidebarMenuPortImpl implements SidebarMenuPort {

  private final BoPortalFeignClient boPortalFeignClient;

  @Override
  public List<SidebarMenuCoreResponse> getSidebarMenusByRoles(List<String> roles) {
    var response = boPortalFeignClient.getSidebarMenus(new BoPortalSidebarMenuRequest(null, roles));
    if (response == null || response.data() == null) {
      return Collections.emptyList();
    }
    return response.data().stream().map(this::toCoreResponse).toList();
  }

  private SidebarMenuCoreResponse toCoreResponse(BoPortalSidebarMenuResponse response) {
    return SidebarMenuCoreResponse.builder()
        .id(response.getId())
        .code(response.getCode())
        .title(response.getTitle())
        .path(response.getPath())
        .icon(response.getIcon())
        .parentId(response.getParentId())
        .roleCodes(response.getRoleCodes())
        .sortOrder(response.getSortOrder())
        .status(response.getStatus())
        .children(response.getChildren() == null
            ? Collections.emptyList()
            : response.getChildren().stream().map(this::toCoreResponse).toList())
        .build();
  }
}
