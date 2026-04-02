package com.fis.hrmservice.domain.port.output.orgchart;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.intern.hub.library.common.dto.PaginatedData;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrgChartNodeRepositoryPort {

  Optional<UserModel> findRootUser();

  Optional<UserModel> findUserInOrgChart(Long userId);

  Optional<Long> findParentUserId(Long userId);

  void initializeRoot(Long userId);

  PaginatedData<UserModel> findDirectSubordinates(Long managerId, int page, int size);

  long countDirectSubordinates(Long managerId);

  List<UserModel> findDirectSubordinatesLimited(Long managerId, int limit);

  PaginatedData<UserModel> searchOrgChartUsers(
      String query, String department, String status, int page, int size);

  PaginatedData<UserModel> findAssignableUsers(String query, int page, int size);

  void assignUsersToManager(List<Long> userIds, Long managerId);

  void removeUsers(List<Long> userIds);

  boolean existsChildren(Long userId);

  boolean isRoot(Long userId);

  Map<Long, Long> countDirectSubordinatesByManagerIds(List<Long> managerIds);
}
