package com.fis.hrmservice.domain.port.output.network;

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
   * @param latitude  user's latitude
   * @param longitude user's longitude
   * @return true if within range of any location, false otherwise
   */
  boolean isAtCompanyLocation(Double latitude, Double longitude);
}
