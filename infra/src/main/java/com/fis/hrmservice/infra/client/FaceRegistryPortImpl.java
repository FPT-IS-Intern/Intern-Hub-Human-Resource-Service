package com.fis.hrmservice.infra.client;

import com.fis.hrmservice.domain.port.output.feign.FaceRegistryPort;
import com.fis.hrmservice.domain.usecase.command.user.RegisterFaceCommand;
import com.fis.hrmservice.infra.model.FaceRegistryApiResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class FaceRegistryPortImpl implements FaceRegistryPort {

  private final RestTemplate restTemplate;
  private final String apiUrl;
  private final String apiKey;

  private static final String FACE_REGISTERED_SUCCESS_MSG = "Face registered successfully";

  public FaceRegistryPortImpl(
      @Qualifier("faceRegistryRestTemplate") RestTemplate faceRegistryRestTemplate,
      @Value("${face-recognition.api-url}") String apiUrl,
      @Value("${face-recognition.api-key}") String apiKey) {
    this.restTemplate = faceRegistryRestTemplate;
    this.apiUrl = apiUrl;
    this.apiKey = apiKey;
  }

  @Override
  public boolean registerFaces(RegisterFaceCommand command) {
    List<MultipartFile> files = command.getFiles();
    if (files == null || files.isEmpty()) {
      log.warn("No face images provided for userName={}", command.getUserName());
      return false;
    }

    boolean allSuccess = true;

    for (MultipartFile file : files) {
      try {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("X-API-KEY", apiKey);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("userName", command.getUserName());

        String originalFilename = file.getOriginalFilename() != null
            ? file.getOriginalFilename()
            : "face.jpg";

        ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
          @Override
          public String getFilename() {
            return originalFilename;
          }
        };
        body.add("files", fileResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<FaceRegistryApiResponse> response = restTemplate.exchange(
            apiUrl,
            HttpMethod.POST,
            requestEntity,
            FaceRegistryApiResponse.class);

        FaceRegistryApiResponse responseBody = response.getBody();

        if (responseBody == null || responseBody.getData() == null || responseBody.getData().isEmpty()) {
          log.warn("Empty response from face registry API for file={}", originalFilename);
          allSuccess = false;
          continue;
        }

        FaceRegistryApiResponse.FaceRegistryDataItem item = responseBody.getData().getFirst();
        if (!FACE_REGISTERED_SUCCESS_MSG.equals(item.getMessage())) {
          log.warn("Face not registered for file={}, message={}", originalFilename, item.getMessage());
          allSuccess = false;
        }

      } catch (Exception e) {
        log.error("Error registering face image={} for userName={}: {}",
            file.getOriginalFilename(), command.getUserName(), e.getMessage(), e);
        allSuccess = false;
      }
    }

    return allSuccess;
  }
}

