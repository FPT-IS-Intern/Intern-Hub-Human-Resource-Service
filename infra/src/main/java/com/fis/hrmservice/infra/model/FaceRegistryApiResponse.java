package com.fis.hrmservice.infra.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FaceRegistryApiResponse {

  private boolean success;
  private String status;
  private String message;
  private List<FaceRegistryDataItem> data;

  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class FaceRegistryDataItem {
    private String userName;
    private String imagePath;
    private String message;
    private boolean registered_status;
  }
}

