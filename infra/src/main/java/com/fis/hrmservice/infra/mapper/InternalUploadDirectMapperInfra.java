package com.fis.hrmservice.infra.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InternalUploadDirectMapperInfra {

  com.fis.hrmservice.domain.utils.response.InternalUploadDirectResponse toDomain(
      com.fis.hrmservice.infra.model.InternalUploadDirectResponse source);
}
