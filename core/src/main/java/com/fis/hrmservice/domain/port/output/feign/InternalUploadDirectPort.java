package com.fis.hrmservice.domain.port.output.feign;

import com.fis.hrmservice.domain.utils.response.InternalUploadDirectResponse;
import org.springframework.web.multipart.MultipartFile;

public interface InternalUploadDirectPort {

  /**
   * Upload file to DMS microservice via internal API.
   *
   * @param file the multipart file to upload
   * @param destinationPath the storage destination path (e.g., "avatars/12345")
   * @param actorId the user ID performing the upload
   * @return upload result with file metadata (objectKey, url, etc.)
   */
  InternalUploadDirectResponse upload(MultipartFile file, String destinationPath, Long actorId);
}
