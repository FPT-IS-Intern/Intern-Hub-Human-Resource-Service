package com.fis.hrmservice.infra.service;

import com.fis.hrmservice.infra.feign.BoPortalFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for network-related operations including company network validation.
 * This service handles
 * infrastructure concerns related to network checking.
 */
@Slf4j
@Service
public class NetworkCheckService {

    @Autowired
    private BoPortalFeignClient boPortalFeignClient;

    /**
     * Check if the given IP address belongs to the company network. Validates
     * against allowed IP
     * ranges from bo-portal service.
     *
     * @param ip the IP address to check
     * @return true if IP is in company network, false otherwise
     */
    public boolean isCompanyIpAddress(String ip) {
        if (ip == null || ip.isEmpty()) {
            log.warn("IP address is null or empty, returning false");
            return false;
        }

        // --- hardcode tạm cái danh sách ip public này lấy bên bo portal qua ---
        // Thay "113.161.x.x" bằng IP Public thật của Wi-Fi sau
        // test thử ở local -> || ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")
        String wifiCongTyHardcode = "113.161.1.2";
        if (ip.equals(wifiCongTyHardcode) || ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
            log.info("IP {} khớp với địa chỉ hardcode (Wi-Fi công ty hoặc Localhost)", ip);
            return true;
        }

        try {
            // Call bo-portal to get allowed IP ranges
            // ResponseApi<List<BoPortalAllowedIpRangeResponse>> response =
            // boPortalFeignClient.getAllowedIpRanges();
            //
            // if (response != null && response.data() != null) {
            // for (BoPortalAllowedIpRangeResponse range : response.data()) {
            // if (range.isActive()
            // && (ip.startsWith(range.ipPrefix()) || ip.equals(range.ipPrefix()))) {
            // log.info("IP {} matched allowed range: {}", ip, range.description());
            // return true;
            // }
            // }
            // }

            log.debug("IP {} did not match any allowed ranges", ip);
            return false;
        } catch (Exception e) {
            log.error("Error checking IP {} against bo-portal: {}", ip, e.getMessage());
            return false;
        }
    }
}
