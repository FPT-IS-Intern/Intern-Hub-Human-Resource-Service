package com.fis.hrmservice.infra.feign.response;

public record DmsDocumentClientModel(
        Long id,
        String objectKey,
        String originalFileName,
        String contentType,
        Long fileSize,
        Object status,
        Long actorId,
        Integer version,
        Object createdAt,
        Object updatedAt) {
}