package com.fis.hrmservice.domain.port.output.user;

import org.springframework.web.multipart.MultipartFile;

public interface FileStoragePort {
  String uploadFile(
          MultipartFile file, String keyPrefix, Long actorId, Long maxSizeBytes, String contentTypeRegex);
}
