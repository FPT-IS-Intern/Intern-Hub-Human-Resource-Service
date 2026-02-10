package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.infra.persistence.entity.Position;
import com.fis.hrmservice.infra.persistence.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-10T10:06:37+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
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
        userModel.fullName( entity.getFullName() );
        userModel.companyEmail( entity.getCompanyEmail() );
        userModel.phoneNumber( entity.getPhoneNumber() );
        userModel.idNumber( entity.getIdNumber() );
        userModel.dateOfBirth( entity.getDateOfBirth() );
        userModel.address( entity.getAddress() );
        userModel.sysStatus( entity.getSysStatus() );
        userModel.internshipStartDate( entity.getInternshipStartDate() );
        userModel.internshipEndDate( entity.getInternshipEndDate() );
        userModel.position( positionToPositionModel( entity.getPosition() ) );

        return userModel.build();
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
        user.setDateOfBirth( model.getDateOfBirth() );
        user.setCreatedAt( model.getCreatedAt() );
        user.setUpdatedAt( model.getUpdatedAt() );
        if ( model.getCreatedBy() != null ) {
            user.setCreatedBy( Long.parseLong( model.getCreatedBy() ) );
        }
        if ( model.getUpdatedBy() != null ) {
            user.setUpdatedBy( Long.parseLong( model.getUpdatedBy() ) );
        }
        user.setAddress( model.getAddress() );
        user.setCompanyEmail( model.getCompanyEmail() );
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

    protected PositionModel positionToPositionModel(Position position) {
        if ( position == null ) {
            return null;
        }

        PositionModel.PositionModelBuilder positionModel = PositionModel.builder();

        positionModel.name( position.getName() );
        positionModel.description( position.getDescription() );

        return positionModel.build();
    }
}
