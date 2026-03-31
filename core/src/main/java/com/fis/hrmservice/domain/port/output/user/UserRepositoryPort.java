package com.fis.hrmservice.domain.port.output.user;

import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.command.user.FilterUserCommand;
import com.intern.hub.library.common.dto.PaginatedData;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
  UserModel save(UserModel user);

  Optional<UserModel> findById(Long userId);

  Optional<UserModel> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByIdNumber(String idNumber);

  List<UserModel> findAll();

  PaginatedData<UserModel> filterUser(FilterUserCommand command, int page, int size);

  int updateStatus(Long userId, UserStatus status);

  UserModel internalUserProfile(Long userId);

  int suspendUser(Long userId, UserStatus status);

  int totalIntern();

  int internshipChanging();

  List<UserModel> listAllSupervisor();

  int updateIsFaceRegistry(Long userId, boolean isFaceRegistry);

  UserModel create(UserModel user);

  List<UserModel> listMemberListBySupervisorId(Long supervisorId);

  List<UserModel> findAllActiveUsers();

  int assignMentor(Long userId, Long mentorId);

  int clearMentor(Long userId);

  List<Long> getAllUserId();

  List<UserModel> searchByQuery(String query);

  Optional<UserModel> findOrgChartRoot();

  PaginatedData<UserModel> findDirectSubordinates(Long managerId, int page, int size);

  long countDirectSubordinates(Long managerId);

  List<UserModel> findDirectSubordinatesLimited(Long managerId, int limit);

  PaginatedData<UserModel> searchOrgChartUsers(
      String query, String department, String status, int page, int size);

  PaginatedData<UserModel> findAssignableOrgChartUsers(Long rootUserId, String query, int page, int size);

  int bulkAssignMentor(List<Long> userIds, Long mentorId);

  int bulkClearMentor(List<Long> userIds);

  java.util.Map<Long, Long> countDirectSubordinatesByManagerIds(List<Long> managerIds);
}
