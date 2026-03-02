package com.fis.hrmservice.infra.feign;

import com.fis.hrmservice.infra.model.AttendanceLocationResponse;
import com.fis.hrmservice.infra.model.BoPortalAllowedIpRangeResponse;
import com.intern.hub.library.common.dto.ResponseApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "bo-portal", url = "${feign.client.config.bo-portal.url}")
public interface BoPortalFeignClient {

    @GetMapping("/bo-portal/internal/allowed-ip-ranges")
    ResponseApi<List<BoPortalAllowedIpRangeResponse>> getAllowedIpRanges();

    @GetMapping("/bo-portal/internal/attendance-locations")
    ResponseApi<List<AttendanceLocationResponse>> getAttendanceLocations();
}
