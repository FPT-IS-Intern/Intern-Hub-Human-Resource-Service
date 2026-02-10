package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.FilterRequest;
import com.fis.hrmservice.api.dto.request.RegisterUserRequest;
import com.fis.hrmservice.api.dto.request.UpdateProfileRequest;
import com.fis.hrmservice.api.dto.response.FilterResponse;
import com.fis.hrmservice.api.dto.response.InternalUserProfileResponse;
import com.fis.hrmservice.api.dto.response.ProfileResponse;
import com.fis.hrmservice.api.dto.response.UserResponse;
import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.user.AvatarModel;
import com.fis.hrmservice.domain.model.user.CvModel;
import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.command.user.FilterUserCommand;
import com.fis.hrmservice.domain.usecase.command.user.RegisterUserCommand;
import com.fis.hrmservice.domain.usecase.command.user.UpdateUserProfileCommand;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-10T10:06:38+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class UserApiMapperImpl implements UserApiMapper {

    @Override
    public RegisterUserCommand toCommand(RegisterUserRequest request) {
        if ( request == null ) {
            return null;
        }

        RegisterUserCommand.RegisterUserCommandBuilder registerUserCommand = RegisterUserCommand.builder();

        if ( request.getEmail() != null ) {
            registerUserCommand.email( request.getEmail() );
        }
        if ( request.getFullName() != null ) {
            registerUserCommand.fullName( request.getFullName() );
        }
        if ( request.getIdNumber() != null ) {
            registerUserCommand.idNumber( request.getIdNumber() );
        }
        if ( request.getBirthDate() != null ) {
            registerUserCommand.birthDate( request.getBirthDate() );
        }
        if ( request.getAddress() != null ) {
            registerUserCommand.address( request.getAddress() );
        }
        if ( request.getPhoneNumber() != null ) {
            registerUserCommand.phoneNumber( request.getPhoneNumber() );
        }
        if ( request.getPositionCode() != null ) {
            registerUserCommand.positionCode( request.getPositionCode() );
        }
        if ( request.getInternshipStartDate() != null ) {
            registerUserCommand.internshipStartDate( request.getInternshipStartDate() );
        }
        if ( request.getInternshipEndDate() != null ) {
            registerUserCommand.internshipEndDate( request.getInternshipEndDate() );
        }

        registerUserCommand.cvFileName( getFileName(request.getCv()) );
        registerUserCommand.cvContentType( getContentType(request.getCv()) );
        registerUserCommand.cvSize( getFileSize(request.getCv()) );
        registerUserCommand.avatarFileName( getFileName(request.getAvatar()) );
        registerUserCommand.avatarContentType( getContentType(request.getAvatar()) );
        registerUserCommand.avatarSize( getFileSize(request.getAvatar()) );

        return registerUserCommand.build();
    }

    @Override
    public UserResponse toResponse(UserModel model) {
        if ( model == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        if ( model.getCompanyEmail() != null ) {
            userResponse.email( model.getCompanyEmail() );
        }
        if ( model.getDateOfBirth() != null ) {
            userResponse.dateOfBirth( model.getDateOfBirth() );
        }
        if ( model.getSysStatus() != null ) {
            userResponse.status( model.getSysStatus().name() );
        }
        String avatarUrl = modelAvatarAvatarUrl( model );
        if ( avatarUrl != null ) {
            userResponse.avatarUrl( avatarUrl );
        }
        String cvUrl = modelCvCvUrl( model );
        if ( cvUrl != null ) {
            userResponse.cvUrl( cvUrl );
        }
        String name = modelPositionName( model );
        if ( name != null ) {
            userResponse.positionCode( name );
        }
        String fullName = modelMentorFullName( model );
        if ( fullName != null ) {
            userResponse.superVisorName( fullName );
        }
        if ( model.getAddress() != null ) {
            userResponse.address( model.getAddress() );
        }
        if ( model.getFullName() != null ) {
            userResponse.fullName( model.getFullName() );
        }
        if ( model.getIdNumber() != null ) {
            userResponse.idNumber( model.getIdNumber() );
        }
        if ( model.getInternshipEndDate() != null ) {
            userResponse.internshipEndDate( model.getInternshipEndDate() );
        }
        if ( model.getInternshipStartDate() != null ) {
            userResponse.internshipStartDate( model.getInternshipStartDate() );
        }
        if ( model.getPhoneNumber() != null ) {
            userResponse.phoneNumber( model.getPhoneNumber() );
        }
        if ( model.getUserId() != null ) {
            userResponse.userId( model.getUserId() );
        }

        return userResponse.build();
    }

    @Override
    public FilterResponse toFilterResponse(UserModel model) {
        if ( model == null ) {
            return null;
        }

        FilterResponse.FilterResponseBuilder filterResponse = FilterResponse.builder();

        if ( model.getCompanyEmail() != null ) {
            filterResponse.email( model.getCompanyEmail() );
        }
        String name = modelPositionName( model );
        if ( name != null ) {
            filterResponse.position( name );
        }
        String avatarUrl = modelAvatarAvatarUrl( model );
        if ( avatarUrl != null ) {
            filterResponse.avatarUrl( avatarUrl );
        }
        if ( model.getFullName() != null ) {
            filterResponse.fullName( model.getFullName() );
        }
        if ( model.getSysStatus() != null ) {
            filterResponse.sysStatus( model.getSysStatus().name() );
        }
        if ( model.getUserId() != null ) {
            filterResponse.userId( model.getUserId() );
        }

        return filterResponse.build();
    }

    @Override
    public ProfileResponse toProfileResponse(UserModel model) {
        if ( model == null ) {
            return null;
        }

        ProfileResponse.ProfileResponseBuilder profileResponse = ProfileResponse.builder();

        String name = modelPositionName( model );
        if ( name != null ) {
            profileResponse.position( name );
        }
        String fullName = modelMentorFullName( model );
        if ( fullName != null ) {
            profileResponse.superVisorName( fullName );
        }
        if ( model.getAddress() != null ) {
            profileResponse.address( model.getAddress() );
        }
        if ( model.getCompanyEmail() != null ) {
            profileResponse.companyEmail( model.getCompanyEmail() );
        }
        if ( model.getDateOfBirth() != null ) {
            profileResponse.dateOfBirth( model.getDateOfBirth() );
        }
        if ( model.getFullName() != null ) {
            profileResponse.fullName( model.getFullName() );
        }
        if ( model.getIdNumber() != null ) {
            profileResponse.idNumber( model.getIdNumber() );
        }
        if ( model.getInternshipEndDate() != null ) {
            profileResponse.internshipEndDate( model.getInternshipEndDate() );
        }
        if ( model.getInternshipStartDate() != null ) {
            profileResponse.internshipStartDate( model.getInternshipStartDate() );
        }
        if ( model.getPhoneNumber() != null ) {
            profileResponse.phoneNumber( model.getPhoneNumber() );
        }
        if ( model.getSysStatus() != null ) {
            profileResponse.sysStatus( model.getSysStatus().name() );
        }

        return profileResponse.build();
    }

    @Override
    public InternalUserProfileResponse toInternalUserProfile(UserModel model) {
        if ( model == null ) {
            return null;
        }

        InternalUserProfileResponse internalUserProfileResponse = new InternalUserProfileResponse();

        if ( model.getCompanyEmail() != null ) {
            internalUserProfileResponse.setCompanyEmail( model.getCompanyEmail() );
        }
        if ( model.getIdNumber() != null ) {
            internalUserProfileResponse.setIdNumber( model.getIdNumber() );
        }
        if ( model.getUserId() != null ) {
            internalUserProfileResponse.setUserId( model.getUserId() );
        }

        return internalUserProfileResponse;
    }

    @Override
    public List<FilterResponse> toFilterResponseList(List<UserModel> userModelList) {
        if ( userModelList == null ) {
            return null;
        }

        List<FilterResponse> list = new ArrayList<FilterResponse>( userModelList.size() );
        for ( UserModel userModel : userModelList ) {
            list.add( toFilterResponse( userModel ) );
        }

        return list;
    }

    @Override
    public UpdateUserProfileCommand toUpdateUserProfileCommand(UpdateProfileRequest request) {
        if ( request == null ) {
            return null;
        }

        UpdateUserProfileCommand.UpdateUserProfileCommandBuilder updateUserProfileCommand = UpdateUserProfileCommand.builder();

        if ( request.getFullName() != null ) {
            updateUserProfileCommand.fullName( request.getFullName() );
        }
        if ( request.getCompanyEmail() != null ) {
            updateUserProfileCommand.companyEmail( request.getCompanyEmail() );
        }
        if ( request.getDateOfBirth() != null ) {
            updateUserProfileCommand.dateOfBirth( request.getDateOfBirth() );
        }
        if ( request.getIdNumber() != null ) {
            updateUserProfileCommand.idNumber( request.getIdNumber() );
        }
        if ( request.getAddress() != null ) {
            updateUserProfileCommand.address( request.getAddress() );
        }
        if ( request.getPhoneNumber() != null ) {
            updateUserProfileCommand.phoneNumber( request.getPhoneNumber() );
        }
        if ( request.getCvFile() != null ) {
            updateUserProfileCommand.cvFile( request.getCvFile() );
        }
        if ( request.getAvatarFile() != null ) {
            updateUserProfileCommand.avatarFile( request.getAvatarFile() );
        }

        return updateUserProfileCommand.build();
    }

    @Override
    public FilterUserCommand toCommand(FilterRequest request) {
        if ( request == null ) {
            return null;
        }

        FilterUserCommand.FilterUserCommandBuilder filterUserCommand = FilterUserCommand.builder();

        if ( request.getKeyword() != null ) {
            filterUserCommand.keyword( request.getKeyword() );
        }
        List<UserStatus> list = request.getSysStatuses();
        if ( list != null ) {
            filterUserCommand.sysStatuses( new ArrayList<UserStatus>( list ) );
        }
        List<String> list1 = request.getRoles();
        if ( list1 != null ) {
            filterUserCommand.roles( new ArrayList<String>( list1 ) );
        }
        List<String> list2 = request.getPositions();
        if ( list2 != null ) {
            filterUserCommand.positions( new ArrayList<String>( list2 ) );
        }

        return filterUserCommand.build();
    }

    private String modelAvatarAvatarUrl(UserModel userModel) {
        AvatarModel avatar = userModel.getAvatar();
        if ( avatar == null ) {
            return null;
        }
        return avatar.getAvatarUrl();
    }

    private String modelCvCvUrl(UserModel userModel) {
        CvModel cv = userModel.getCv();
        if ( cv == null ) {
            return null;
        }
        return cv.getCvUrl();
    }

    private String modelPositionName(UserModel userModel) {
        PositionModel position = userModel.getPosition();
        if ( position == null ) {
            return null;
        }
        return position.getName();
    }

    private String modelMentorFullName(UserModel userModel) {
        UserModel mentor = userModel.getMentor();
        if ( mentor == null ) {
            return null;
        }
        return mentor.getFullName();
    }
}
