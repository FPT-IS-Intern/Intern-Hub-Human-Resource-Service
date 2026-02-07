package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.ticket.RemoteRequestModel;
import com.fis.hrmservice.domain.model.constant.RemoteType;
import com.fis.hrmservice.infra.persistence.entity.RemoteRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RemoteRequestMapper {

    /* ========= Model → Entity ========= */
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "ticket", target = "tickets")
    @Mapping(source = "remoteType", target = "remoteType", qualifiedByName = "remoteTypeToString")
    @Mapping(target = "workLocation", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    RemoteRequest toEntity(RemoteRequestModel model);

    /* ========= Entity → Model ========= */
    @Mapping(source = "tickets", target = "ticket")
    @Mapping(source = "remoteType", target = "remoteType", qualifiedByName = "stringToRemoteType")
    RemoteRequestModel toModel(RemoteRequest entity);

    /* ========= Custom enum mapper ========= */

    @Named("remoteTypeToString")
    default String remoteTypeToString(RemoteType type) {
        return type == null ? null : type.name();
    }

    @Named("stringToRemoteType")
    default RemoteType stringToRemoteType(String type) {
        return type == null ? null : RemoteType.valueOf(type);
    }
}
