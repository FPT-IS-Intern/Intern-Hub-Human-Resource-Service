package com.fis.hrmservice.infra.storage;

import com.fis.hrmservice.domain.port.output.user.FileStoragePort;
import com.fis.hrmservice.infra.feign.response.DmsDocumentClientModel;
import com.fis.hrmservice.infra.feign.client.InternalUploadDirectClient;
import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.exception.InternalErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageAdapter implements FileStoragePort {

  final InternalUploadDirectClient dmsInternalFeignClient;

  public String uploadFile(
          MultipartFile file, String keyPrefix, Long actorId,
          Long maxSizeBytes, String contentTypeRegex
  ) {

    if (file.getSize() > maxSizeBytes) {
      throw new BadRequestException(
              "file.size.exceeded",
              "Dung lượng file vượt quá giới hạn " + (maxSizeBytes / 1024 / 1024) + "MB");
    }

    String contentType = file.getContentType();
    if (contentType == null || !contentType.matches(contentTypeRegex)) {
      throw new BadRequestException(
              "file.type.invalid", "Định dạng file không hợp lệ. Yêu cầu: " + contentTypeRegex);
    }

    try {
      ResponseApi<DmsDocumentClientModel> response =
              dmsInternalFeignClient.uploadFile(file, keyPrefix, actorId, false);

      if (response == null || response.data() == null || !hasText(response.data().objectKey())) {
        throw new InternalErrorException(
                "storage.upload.error", "DMS không trả về thông tin file sau khi upload");
      }

      return response.data().objectKey();
    } catch (Exception e) {
      log.error("DMS upload failed for destination path {}", keyPrefix, e);
      throw new InternalErrorException(
              "storage.upload.error", "Không thể upload file lên hệ thống lưu trữ");
    }
  }

  private static boolean hasText(String value) {
    return value != null && !value.isBlank();
  }
}
