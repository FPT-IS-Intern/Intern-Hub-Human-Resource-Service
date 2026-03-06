package com.fis.hrmservice.infra.service;

import com.fis.hrmservice.domain.port.output.network.NetworkCheckPort;
import com.fis.hrmservice.infra.feign.BoPortalFeignClient;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service for network-related operations including company network validation. This service handles
 * infrastructure concerns related to network checking.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NetworkCheckService implements NetworkCheckPort {

  private final BoPortalFeignClient boPortalFeignClient;

  /**
   * Check if the given IP address belongs to the company network. Validates against allowed IP
   * ranges from bo-portal service.
   *
   * @param ip the IP address to check
   * @return true if IP is in company network, false otherwise
   */
  public boolean isCompanyIpAddress(String ip) {
    return resolveCompanyIpBranchId(ip).isPresent();
  }

  @Override
  public Optional<UUID> resolveCompanyIpBranchId(String ip) {
    if (ip == null || ip.isEmpty()) {
      log.warn("IP address is null or empty, returning false");
      return Optional.empty();
    }

    try {
      var response = boPortalFeignClient.getAllowedIpRanges();
      if (response != null && response.data() != null) {
        for (var range : response.data()) {
          if (range.isActive() && ip.startsWith(range.ipPrefix())) {
            log.info("IP {} matched allowed range: {}", ip, range.description());
            return Optional.ofNullable(range.branchId());
          }
        }
      }
      return Optional.empty();
    } catch (Exception e) {
      log.error("Error checking IP {} against bo-portal: {}", ip, e.getMessage());
      return Optional.empty();
    }
  }

  @Override
  public boolean isAtCompanyLocation(Double latitude, Double longitude) {
    return resolveCompanyLocationBranchId(latitude, longitude).isPresent();
  }

  @Override
  public Optional<UUID> resolveCompanyLocationBranchId(Double latitude, Double longitude) {
    if (latitude == null || longitude == null) {
      log.warn("Coordinates are null, returning false");
      return Optional.empty();
    }

    try {
      var response = boPortalFeignClient.getAttendanceLocations();
      if (response != null && response.data() != null) {
        for (var loc : response.data()) {
          double distance = calculateDistance(latitude, longitude, loc.latitude(), loc.longitude());
          if (distance <= loc.radiusMeters()) {
            log.info("User is at location: {} (distance: {}m)", loc.name(), (int) distance);
            return Optional.ofNullable(loc.branchId());
          }
        }
      }
    } catch (Exception e) {
      log.error("Error fetching attendance locations from bo-portal: {}", e.getMessage());
    }
    return Optional.empty();
  }

  @Override
  public Optional<String> resolveBranchName(UUID branchId) {
    if (branchId == null) {
      return Optional.empty();
    }

    try {
      var locationResponse = boPortalFeignClient.getAttendanceLocations();
      if (locationResponse != null && locationResponse.data() != null) {
        for (var loc : locationResponse.data()) {
          if (branchId.equals(loc.branchId())) {
            return Optional.ofNullable(loc.name());
          }
        }
      }

      var ipRangeResponse = boPortalFeignClient.getAllowedIpRanges();
      if (ipRangeResponse != null && ipRangeResponse.data() != null) {
        for (var range : ipRangeResponse.data()) {
          if (branchId.equals(range.branchId())) {
            return Optional.ofNullable(range.name());
          }
        }
      }
    } catch (Exception e) {
      log.error("Error resolving branch name for branchId {}: {}", branchId, e.getMessage());
    }

    return Optional.empty();
  }

  private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    double R = 6371e3; // Earth radius in meters
    double phi1 = Math.toRadians(lat1);
    double phi2 = Math.toRadians(lat2);
    double deltaPhi = Math.toRadians(lat2 - lat1);
    double deltaLambda = Math.toRadians(lon2 - lon1);

    double a =
        Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2)
            + Math.cos(phi1)
                * Math.cos(phi2)
                * Math.sin(deltaLambda / 2)
                * Math.sin(deltaLambda / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return R * c;
  }
}
