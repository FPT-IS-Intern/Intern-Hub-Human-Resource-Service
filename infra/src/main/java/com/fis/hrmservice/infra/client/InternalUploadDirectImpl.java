package com.fis.hrmservice.infra.client;

import com.fis.hrmservice.domain.port.output.feign.InternalUploadDirectPort;
import com.fis.hrmservice.domain.utils.response.InternalUploadDirectResponse;
import com.fis.hrmservice.infra.feign.InternalUploadDirectClient;
import com.fis.hrmservice.infra.mapper.InternalUploadDirectMapperInfra;
import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.library.common.exception.ConflictDataException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalUploadDirectImpl implements InternalUploadDirectPort {

  InternalUploadDirectClient internalUploadDirectClient;
  InternalUploadDirectMapperInfra mapper;

  @Override
  public InternalUploadDirectResponse upload(
      MultipartFile file, String destinationPath, Long actorId) {

    log.info(
        "Uploading file via DMS: name={}, size={}, dest={}, actor={}",
        file.getOriginalFilename(),
        file.getSize(),
        destinationPath,
        actorId);

    try {
      ResponseApi<com.fis.hrmservice.infra.model.InternalUploadDirectResponse> response =
          internalUploadDirectClient.uploadDirect(file, destinationPath, actorId);

      if (response == null || response.data() == null) {
        throw new ConflictDataException("DMS upload returned empty response");
      }

      InternalUploadDirectResponse result = mapper.toDomain(response.data());

      log.info(
          "DMS upload success: objectKey={}, fileSize={}",
          result.getObjectKey(),
          result.getFileSize());

      return result;

    } catch (ConflictDataException e) {
      throw e;
    } catch (Exception e) {
      log.error("DMS upload failed for file: {}", file.getOriginalFilename(), e);
      throw new ConflictDataException("File upload to DMS failed: " + e.getMessage());
    }
  }
}
