package com.fis.hrmservice.infra.feign;

import com.fis.hrmservice.infra.model.BoPortalAllowedIpRangeResponse;
import com.intern.hub.library.common.dto.ResponseApi;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "bo-portal", url = "${feign.client.config.bo-portal.url:http://bo-portal:8080}")
public interface BoPortalFeignClient {

  @GetMapping("/bo-portal/internal/allowed-ip-ranges")
  ResponseApi<List<BoPortalAllowedIpRangeResponse>> getAllowedIpRanges();
}
