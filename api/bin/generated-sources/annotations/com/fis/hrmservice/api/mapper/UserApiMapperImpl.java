package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.FilterRequest;
import com.fis.hrmservice.api.dto.request.RegisterUserRequest;
import com.fis.hrmservice.api.dto.request.UpdateProfileRequest;
import com.fis.hrmservice.api.dto.response.FilterResponse;
import com.fis.hrmservice.api.dto.response.HrmUserSearchResponse;
import com.fis.hrmservice.api.dto.response.InternalUserProfileResponse;
import com.fis.hrmservice.api.dto.response.InternalUserResponse;
import com.fis.hrmservice.api.dto.response.SidebarMenuResponse;
import com.fis.hrmservice.api.dto.response.SupervisorMemberResponse;
import com.fis.hrmservice.api.dto.response.SupervisorResponse;
import com.fis.hrmservice.api.dto.response.UserResponse;
import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.resonse.InternalUserCoreResponse;
import com.fis.hrmservice.domain.model.resonse.SidebarMenuCoreResponse;
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
    date = "2026-03-23T17:39:09+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
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
        if ( request.getAvatar() != null ) {
            registerUserCommand.avatar( request.getAvatar() );
        }
        if ( request.getCv() != null ) {
            registerUserCommand.cv( request.getCv() );
        }

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
        String name = modelPositionName( model );
        if ( name != null ) {
            userResponse.positionCode( name );
        }
        Long userId = modelMentorUserId( model );
        if ( userId != null ) {
            userResponse.superVisorId( String.valueOf( userId ) );
        }
        if ( model.getAddress() != null ) {
            userResponse.address( model.getAddress() );
        }
        if ( model.getAvatarUrl() != null ) {
            userResponse.avatarUrl( model.getAvatarUrl() );
        }
        if ( model.getCvUrl() != null ) {
            userResponse.cvUrl( model.getCvUrl() );
        }
        if ( model.getDateOfBirth() != null ) {
            userResponse.dateOfBirth( model.getDateOfBirth() );
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
        if ( model.getRoleId() != null ) {
            userResponse.roleId( model.getRoleId() );
        }
        if ( model.getSysStatus() != null ) {
            userResponse.sysStatus( model.getSysStatus().name() );
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
        if ( model.getAvatarUrl() != null ) {
            filterResponse.avatarUrl( model.getAvatarUrl() );
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

        filterResponse.position( getDisplayPosition(model.getPosition()) );
        filterResponse.role( getDisplayRole(model.getPosition()) );

        return filterResponse.build();
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
        if ( request.getPosition() != null ) {
            updateUserProfileCommand.position( request.getPosition() );
        }
        if ( request.getSysStatus() != null ) {
            updateUserProfileCommand.sysStatus( request.getSysStatus() );
        }

        return updateUserProfileCommand.build();
    }

    @Override
    public InternalUserResponse toInternalUserResponse(UserModel model) {
        if ( model == null ) {
            return null;
        }

        InternalUserResponse internalUserResponse = new InternalUserResponse();

        if ( model.getCompanyEmail() != null ) {
            internalUserResponse.setEmail( model.getCompanyEmail() );
        }
        String name = modelPositionName( model );
        if ( name != null ) {
            internalUserResponse.setPositionName( name );
        }
        if ( model.getAvatarUrl() != null ) {
            internalUserResponse.setAvatarUrl( model.getAvatarUrl() );
        }
        if ( model.getFullName() != null ) {
            internalUserResponse.setFullName( model.getFullName() );
        }
        if ( model.getIsFaceRegistry() != null ) {
            internalUserResponse.setIsFaceRegistry( model.getIsFaceRegistry() );
        }
        if ( model.getRole() != null ) {
            internalUserResponse.setRole( model.getRole() );
        }
        if ( model.getRoleId() != null ) {
            internalUserResponse.setRoleId( Long.parseLong( model.getRoleId() ) );
        }

        return internalUserResponse;
    }

    @Override
    public InternalUserResponse toInternalUserResponse(InternalUserCoreResponse response) {
        if ( response == null ) {
            return null;
        }

        InternalUserResponse internalUserResponse = new InternalUserResponse();

        if ( response.getAvatarUrl() != null ) {
            internalUserResponse.setAvatarUrl( response.getAvatarUrl() );
        }
        if ( response.getEmail() != null ) {
            internalUserResponse.setEmail( response.getEmail() );
        }
        if ( response.getFullName() != null ) {
            internalUserResponse.setFullName( response.getFullName() );
        }
        if ( response.getIsFaceRegistry() != null ) {
            internalUserResponse.setIsFaceRegistry( response.getIsFaceRegistry() );
        }
        if ( response.getPositionName() != null ) {
            internalUserResponse.setPositionName( response.getPositionName() );
        }
        if ( response.getRole() != null ) {
            internalUserResponse.setRole( response.getRole() );
        }
        if ( response.getRoleId() != null ) {
            internalUserResponse.setRoleId( response.getRoleId() );
        }
        List<SidebarMenuResponse> list = toSidebarMenuResponseList( response.getSidebarMenus() );
        if ( list != null ) {
            internalUserResponse.setSidebarMenus( list );
        }

        return internalUserResponse;
    }

    @Override
    public SidebarMenuResponse toSidebarMenuResponse(SidebarMenuCoreResponse response) {
        if ( response == null ) {
            return null;
        }

        SidebarMenuResponse.SidebarMenuResponseBuilder sidebarMenuResponse = SidebarMenuResponse.builder();

        List<SidebarMenuResponse> list = toSidebarMenuResponseList( response.getChildren() );
        if ( list != null ) {
            sidebarMenuResponse.children( list );
        }
        if ( response.getCode() != null ) {
            sidebarMenuResponse.code( response.getCode() );
        }
        if ( response.getIcon() != null ) {
            sidebarMenuResponse.icon( response.getIcon() );
        }
        if ( response.getId() != null ) {
            sidebarMenuResponse.id( response.getId() );
        }
        if ( response.getParentId() != null ) {
            sidebarMenuResponse.parentId( response.getParentId() );
        }
        if ( response.getPath() != null ) {
            sidebarMenuResponse.path( response.getPath() );
        }
        List<String> list1 = response.getRoleCodes();
        if ( list1 != null ) {
            sidebarMenuResponse.roleCodes( new ArrayList<String>( list1 ) );
        }
        if ( response.getSortOrder() != null ) {
            sidebarMenuResponse.sortOrder( response.getSortOrder() );
        }
        if ( response.getStatus() != null ) {
            sidebarMenuResponse.status( response.getStatus() );
        }
        if ( response.getTitle() != null ) {
            sidebarMenuResponse.title( response.getTitle() );
        }

        return sidebarMenuResponse.build();
    }

    @Override
    public List<SidebarMenuResponse> toSidebarMenuResponseList(List<SidebarMenuCoreResponse> responses) {
        if ( responses == null ) {
            return null;
        }

        List<SidebarMenuResponse> list = new ArrayList<SidebarMenuResponse>( responses.size() );
        for ( SidebarMenuCoreResponse sidebarMenuCoreResponse : responses ) {
            list.add( toSidebarMenuResponse( sidebarMenuCoreResponse ) );
        }

        return list;
    }

    @Override
    public SupervisorResponse toSupervisorResponse(UserModel model) {
        if ( model == null ) {
            return null;
        }

        SupervisorResponse.SupervisorResponseBuilder supervisorResponse = SupervisorResponse.builder();

        if ( model.getFullName() != null ) {
            supervisorResponse.fullName( model.getFullName() );
        }
        if ( model.getFullName() != null ) {
            supervisorResponse.nickName( buildNickName( model.getFullName() ) );
        }
        if ( model.getAvatarUrl() != null ) {
            supervisorResponse.avatarUrl( model.getAvatarUrl() );
        }
        if ( model.getUserId() != null ) {
            supervisorResponse.userId( model.getUserId() );
        }

        return supervisorResponse.build();
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

    @Override
    public SupervisorMemberResponse toSupervisorMemberResponse(UserModel model) {
        if ( model == null ) {
            return null;
        }

        SupervisorMemberResponse.SupervisorMemberResponseBuilder supervisorMemberResponse = SupervisorMemberResponse.builder();

        if ( model.getUserId() != null ) {
            supervisorMemberResponse.userId( model.getUserId() );
        }
        if ( model.getAvatarUrl() != null ) {
            supervisorMemberResponse.avatarUrl( model.getAvatarUrl() );
        }
        if ( model.getFullName() != null ) {
            supervisorMemberResponse.fullName( model.getFullName() );
        }
        if ( model.getSysStatus() != null ) {
            supervisorMemberResponse.sysStatus( model.getSysStatus().name() );
        }
        if ( model.getCompanyEmail() != null ) {
            supervisorMemberResponse.companyEmail( model.getCompanyEmail() );
        }
        String name = modelPositionName( model );
        if ( name != null ) {
            supervisorMemberResponse.position( name );
        }

        return supervisorMemberResponse.build();
    }

    @Override
    public HrmUserSearchResponse toHrmUserSearchResponse(UserModel model) {
        if ( model == null ) {
            return null;
        }

        HrmUserSearchResponse.HrmUserSearchResponseBuilder hrmUserSearchResponse = HrmUserSearchResponse.builder();

        if ( model.getUserId() != null ) {
            hrmUserSearchResponse.id( model.getUserId() );
        }
        if ( model.getCompanyEmail() != null ) {
            hrmUserSearchResponse.email( model.getCompanyEmail() );
        }
        if ( model.getFullName() != null ) {
            hrmUserSearchResponse.fullName( model.getFullName() );
        }

        return hrmUserSearchResponse.build();
    }

    @Override
    public List<HrmUserSearchResponse> toHrmUserSearchResponseList(List<UserModel> models) {
        if ( models == null ) {
            return null;
        }

        List<HrmUserSearchResponse> list = new ArrayList<HrmUserSearchResponse>( models.size() );
        for ( UserModel userModel : models ) {
            list.add( toHrmUserSearchResponse( userModel ) );
        }

        return list;
    }

    private String modelPositionName(UserModel userModel) {
        PositionModel position = userModel.getPosition();
        if ( position == null ) {
            return null;
        }
        return position.getName();
    }

    private Long modelMentorUserId(UserModel userModel) {
        UserModel mentor = userModel.getMentor();
        if ( mentor == null ) {
            return null;
        }
        return mentor.getUserId();
    }
}
