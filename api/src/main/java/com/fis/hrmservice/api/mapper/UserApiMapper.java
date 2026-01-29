package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.RegisterUserRequest;
import com.fis.hrmservice.api.dto.response.UserResponse;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.command.RegisterUserCommand;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserApiMapper {

    @Mapping(target = "cvFileName", expression = "java(getFileName(request.getCv()))")
    @Mapping(target = "cvContentType", expression = "java(getContentType(request.getCv()))")
    @Mapping(target = "cvSize", expression = "java(getFileSize(request.getCv()))")
    @Mapping(target = "avatarFileName", expression = "java(getFileName(request.getAvatar()))")
    @Mapping(target = "avatarContentType", expression = "java(getContentType(request.getAvatar()))")
    @Mapping(target = "avatarSize", expression = "java(getFileSize(request.getAvatar()))")
    RegisterUserCommand toCommand(RegisterUserRequest request);

    @Mapping(source = "companyEmail", target = "email")
    UserResponse toResponse(UserModel model);
}
