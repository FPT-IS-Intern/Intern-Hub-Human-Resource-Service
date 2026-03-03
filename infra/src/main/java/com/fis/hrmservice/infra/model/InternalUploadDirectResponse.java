package com.fis.hrmservice.infra.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InternalUploadDirectResponse {
  Long id;
  String objectKey;
  String originalFileName;
  String contentType;
  Long fileSize;
  String status;
  Long actorId;
  Long version;
  LocalDateTime createdAt;
  LocalDateTime updatedAt;
}
