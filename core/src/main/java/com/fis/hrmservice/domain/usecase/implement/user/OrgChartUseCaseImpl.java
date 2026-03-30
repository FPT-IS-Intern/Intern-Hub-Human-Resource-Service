package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.exception.NotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrgChartUseCaseImpl {

  UserRepositoryPort userRepositoryPort;

  public UserModel getRootUser(Long rootId) {
    if (rootId != null) {
      return getUserOrThrow(rootId);
    }

    return userRepositoryPort
        .findOrgChartRoot()
        .orElseThrow(() -> new NotFoundException("Org chart root user not found"));
  }

  public UserModel getUserOrThrow(Long userId) {
    return userRepositoryPort
        .findById(userId)
        .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
  }

  public List<UserModel> getDirectSubordinates(Long userId, int page, int limit) {
    return castItems(userRepositoryPort.findDirectSubordinates(userId, page, limit));
  }

  public PaginatedData<UserModel> getDirectSubordinatesPage(Long userId, int page, int limit) {
    return userRepositoryPort.findDirectSubordinates(userId, page, limit);
  }

  public List<UserModel> getPreviewSubordinates(Long userId, int limit) {
    return userRepositoryPort.findDirectSubordinatesLimited(userId, limit);
  }

  public PaginatedData<UserModel> searchUsers(
      String query, String department, String status, int page, int limit) {
    return userRepositoryPort.searchOrgChartUsers(normalize(query), normalize(department), normalize(status), page, limit);
  }

  public long countDirectSubordinates(Long userId) {
    return userRepositoryPort.countDirectSubordinates(userId);
  }

  public Map<Long, Long> countDirectSubordinates(List<Long> userIds) {
    return userRepositoryPort.countDirectSubordinatesByManagerIds(userIds);
  }

  public List<UserModel> getPathToRoot(Long userId) {
    LinkedList<UserModel> path = new LinkedList<>();
    Long currentUserId = userId;

    while (currentUserId != null) {
      UserModel currentUser = getUserOrThrow(currentUserId);
      path.addFirst(currentUser);
      currentUserId =
          currentUser.getMentor() != null ? currentUser.getMentor().getUserId() : null;
    }

    return path;
  }

  public UserModel buildTree(UserModel root, int maxDepth) {
    int normalizedDepth = Math.max(maxDepth, 1);
    return buildTreeNode(root, normalizedDepth, 1);
  }

  private UserModel buildTreeNode(UserModel user, int maxDepth, int currentDepth) {
    long subordinateCount = countDirectSubordinates(user.getUserId());
    if (currentDepth >= maxDepth || subordinateCount == 0) {
      return cloneUser(user);
    }

    List<UserModel> subordinates = getDirectSubordinates(user.getUserId(), 0, 1000);
    List<UserModel> childNodes = new ArrayList<>(subordinates.size());
    for (UserModel subordinate : subordinates) {
      childNodes.add(buildTreeNode(subordinate, maxDepth, currentDepth + 1));
    }

    UserModel node = cloneUser(user);
    node.setChildren(childNodes);
    return node;
  }

  private UserModel cloneUser(UserModel user) {
    UserModel cloned =
        UserModel.builder()
            .userId(user.getUserId())
            .fullName(user.getFullName())
            .companyEmail(user.getCompanyEmail())
            .phoneNumber(user.getPhoneNumber())
            .idNumber(user.getIdNumber())
            .dateOfBirth(user.getDateOfBirth())
            .address(user.getAddress())
            .sysStatus(user.getSysStatus())
            .internshipStartDate(user.getInternshipStartDate())
            .internshipEndDate(user.getInternshipEndDate())
            .position(user.getPosition())
            .mentor(user.getMentor())
            .department(user.getDepartment())
            .departmentId(user.getDepartmentId())
            .departmentCode(user.getDepartmentCode())
            .isFaceRegistry(user.getIsFaceRegistry())
            .roleId(user.getRoleId())
            .role(user.getRole())
            .authIdentityStatus(user.getAuthIdentityStatus())
            .avatarUrl(user.getAvatarUrl())
            .cvUrl(user.getCvUrl())
            .build();
    cloned.setChildren(Collections.emptyList());
    return cloned;
  }

  @SuppressWarnings("unchecked")
  private List<UserModel> castItems(PaginatedData<UserModel> paginatedData) {
    return (List<UserModel>) paginatedData.getItems();
  }

  private String normalize(String value) {
    return value == null ? null : value.trim();
  }
}
