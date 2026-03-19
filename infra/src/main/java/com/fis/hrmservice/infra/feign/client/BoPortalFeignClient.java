package com.fis.hrmservice.infra.feign.client;

import com.fis.hrmservice.infra.model.AttendanceLocationResponse;
import com.fis.hrmservice.infra.model.BoPortalAllowedIpRangeResponse;
import com.fis.hrmservice.infra.model.BoPortalSidebarMenuRequest;
import com.fis.hrmservice.infra.model.BoPortalSidebarMenuResponse;
import com.fis.hrmservice.infra.model.BoPortalWorkingTimeConfigResponse;
import com.intern.hub.library.common.dto.ResponseApi;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "bo-portal", url = "${feign.client.config.bo-portal.url}")
public interface BoPortalFeignClient {

  @GetMapping("/bo-portal/internal/allowed-ip-ranges")
  ResponseApi<List<BoPortalAllowedIpRangeResponse>> getAllowedIpRanges();

  @GetMapping("/bo-portal/internal/attendance-locations")
  ResponseApi<List<AttendanceLocationResponse>> getAttendanceLocations();

  @GetMapping("/bo-portal/internal/working-time-config")
  ResponseApi<BoPortalWorkingTimeConfigResponse> getWorkingTimeConfig();

  @PostMapping("/bo-portal/internal/sidebar-menus")
  ResponseApi<List<BoPortalSidebarMenuResponse>> getSidebarMenus(
      @RequestBody BoPortalSidebarMenuRequest request);
}
