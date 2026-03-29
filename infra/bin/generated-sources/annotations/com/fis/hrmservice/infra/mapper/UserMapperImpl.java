package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.infra.persistence.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-29T13:05:03+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserModel toModel(User entity) {
        if ( entity == null ) {
            return null;
        }

        UserModel.UserModelBuilder userModel = UserModel.builder();

        userModel.userId( entity.getId() );
        userModel.mentor( mentorToModel( entity.getMentor() ) );
        userModel.position( positionToModel( entity.getPosition() ) );
        userModel.department( departmentToString( entity.getDepartment() ) );
        userModel.fullName( entity.getFullName() );
        userModel.companyEmail( entity.getCompanyEmail() );
        userModel.phoneNumber( entity.getPhoneNumber() );
        userModel.idNumber( entity.getIdNumber() );
        userModel.dateOfBirth( entity.getDateOfBirth() );
        userModel.address( entity.getAddress() );
        userModel.sysStatus( entity.getSysStatus() );
        userModel.internshipStartDate( entity.getInternshipStartDate() );
        userModel.internshipEndDate( entity.getInternshipEndDate() );
        userModel.isFaceRegistry( entity.getIsFaceRegistry() );
        userModel.avatarUrl( entity.getAvatarUrl() );
        userModel.cvUrl( entity.getCvUrl() );

        return userModel.build();
    }

    @Override
    public void updateEntity(UserModel model, User entity) {
        if ( model == null ) {
            return;
        }

        entity.setMentor( mentorToEntity( model.getMentor() ) );
        entity.setPosition( positionToEntity( model.getPosition() ) );
        entity.setCreatedAt( model.getCreatedAt() );
        entity.setUpdatedAt( model.getUpdatedAt() );
        if ( model.getCreatedBy() != null ) {
            entity.setCreatedBy( Long.parseLong( model.getCreatedBy() ) );
        }
        else {
            entity.setCreatedBy( null );
        }
        if ( model.getUpdatedBy() != null ) {
            entity.setUpdatedBy( Long.parseLong( model.getUpdatedBy() ) );
        }
        else {
            entity.setUpdatedBy( null );
        }
        entity.setAddress( model.getAddress() );
        entity.setAvatarUrl( model.getAvatarUrl() );
        entity.setCompanyEmail( model.getCompanyEmail() );
        entity.setCvUrl( model.getCvUrl() );
        entity.setDateOfBirth( model.getDateOfBirth() );
        entity.setFullName( model.getFullName() );
        entity.setIdNumber( model.getIdNumber() );
        entity.setInternshipEndDate( model.getInternshipEndDate() );
        entity.setInternshipStartDate( model.getInternshipStartDate() );
        entity.setPhoneNumber( model.getPhoneNumber() );
        entity.setSysStatus( model.getSysStatus() );
    }

    @Override
    public List<UserModel> toModelList(List<User> entities) {
        if ( entities == null ) {
            return null;
        }

        List<UserModel> list = new ArrayList<UserModel>( entities.size() );
        for ( User user : entities ) {
            list.add( toModel( user ) );
        }

        return list;
    }

    @Override
    public User toEntity(UserModel model) {
        if ( model == null ) {
            return null;
        }

        User user = new User();

        user.setId( model.getUserId() );
        user.setMentor( mentorToEntity( model.getMentor() ) );
        user.setPosition( positionToEntity( model.getPosition() ) );
        user.setCreatedAt( model.getCreatedAt() );
        user.setUpdatedAt( model.getUpdatedAt() );
        if ( model.getCreatedBy() != null ) {
            user.setCreatedBy( Long.parseLong( model.getCreatedBy() ) );
        }
        if ( model.getUpdatedBy() != null ) {
            user.setUpdatedBy( Long.parseLong( model.getUpdatedBy() ) );
        }
        user.setAddress( model.getAddress() );
        user.setAvatarUrl( model.getAvatarUrl() );
        user.setCompanyEmail( model.getCompanyEmail() );
        user.setCvUrl( model.getCvUrl() );
        user.setDateOfBirth( model.getDateOfBirth() );
        user.setFullName( model.getFullName() );
        user.setIdNumber( model.getIdNumber() );
        user.setInternshipEndDate( model.getInternshipEndDate() );
        user.setInternshipStartDate( model.getInternshipStartDate() );
        user.setPhoneNumber( model.getPhoneNumber() );
        user.setSysStatus( model.getSysStatus() );

        return user;
    }

    @Override
    public List<UserModel> toResponseList(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserModel> list = new ArrayList<UserModel>( users.size() );
        for ( User user : users ) {
            list.add( toModel( user ) );
        }

        return list;
    }
}
