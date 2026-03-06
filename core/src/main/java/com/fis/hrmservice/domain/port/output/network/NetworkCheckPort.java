package com.fis.hrmservice.domain.port.output.network;

import java.util.Optional;
import java.util.UUID;

/** Port for network-related operations. */
public interface NetworkCheckPort {
  /**
   * Check if the given IP address is part of the company network.
   *
   * @param ip the IP address to check
   * @return true if it's a company IP, false otherwise
   */
  boolean isCompanyIpAddress(String ip);

  /**
   * Check if the given coordinates are within range of any company location.
   *
   * @param latitude user's latitude
   * @param longitude user's longitude
   * @return true if within range of any location, false otherwise
   */
  boolean isAtCompanyLocation(Double latitude, Double longitude);

  /**
   * Resolve branchId by matching the given IP against company allowed IP ranges.
   *
   * @param ip the IP address to check
   * @return branchId if matched, empty otherwise
   */
  Optional<UUID> resolveCompanyIpBranchId(String ip);

  /**
   * Resolve branchId by matching the given coordinates against company attendance locations.
   *
   * @param latitude user's latitude
   * @param longitude user's longitude
   * @return branchId if matched, empty otherwise
   */
  Optional<UUID> resolveCompanyLocationBranchId(Double latitude, Double longitude);

  Optional<String> resolveBranchName(UUID branchId);
}
