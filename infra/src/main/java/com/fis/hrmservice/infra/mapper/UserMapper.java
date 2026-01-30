package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.infra.persistence.entity.Position;
import com.fis.hrmservice.infra.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "positionId", source = "position", qualifiedByName = "positionToId")
    @Mapping(target = "mentorId", source = "mentor", qualifiedByName = "mentorToId")
    UserModel toModel(User entity);

    @Mapping(target = "id", source = "userId")
    @Mapping(target = "position", source = "positionId", qualifiedByName = "idToPosition")
    @Mapping(target = "mentor", source = "mentorId", qualifiedByName = "idToMentor")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    User toEntity(UserModel model);


    @Mapping(target = "id", source = "userId")
    @Mapping(target = "positionId", source = "position.id")
    @Mapping(target = "mentorId", source = "mentor.id")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "idNumber", source = "idNumber")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "companyEmail", source = "companyEmail")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "internshipStartDate", source = "internshipStartDate")
    @Mapping(target = "internshipEndDate", source = "internshipEndDate")
    @Mapping(target = "sysStatus", source = "sysStatus")
    List<UserModel> toResponseList(List<User> userEntity);
}
