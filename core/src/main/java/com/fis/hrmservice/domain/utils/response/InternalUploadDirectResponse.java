package com.fis.hrmservice.domain.utils.response;

import lombok.*;

import java.time.LocalDateTime;

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
