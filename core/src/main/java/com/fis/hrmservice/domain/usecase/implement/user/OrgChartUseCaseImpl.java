package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.exception.ConflictDataException;
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
import org.springframework.transaction.annotation.Transactional;

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

  public PaginatedData<UserModel> searchAssignableUsers(String query, int page, int limit) {
    Long rootUserId = userRepositoryPort.findOrgChartRoot().map(UserModel::getUserId).orElse(null);
    return userRepositoryPort.findAssignableOrgChartUsers(rootUserId, normalize(query), page, limit);
  }

  public long countDirectSubordinates(Long userId) {
    return userRepositoryPort.countDirectSubordinates(userId);
  }

  public Map<Long, Long> countDirectSubordinates(List<Long> userIds) {
    return userRepositoryPort.countDirectSubordinatesByManagerIds(userIds);
  }

  @Transactional
  public List<Long> bulkUpdateManager(List<Long> userIds, Long managerId) {
    if (userIds == null || userIds.isEmpty()) {
      throw new ConflictDataException("User ids are required");
    }

    List<Long> normalizedUserIds = userIds.stream().filter(java.util.Objects::nonNull).distinct().toList();
    if (normalizedUserIds.isEmpty()) {
      throw new ConflictDataException("User ids are required");
    }

    UserModel manager = managerId != null ? getUserOrThrow(managerId) : null;
    for (Long userId : normalizedUserIds) {
      UserModel user = getUserOrThrow(userId);
      validateManagerAssignment(user, manager);
    }

    if (managerId == null) {
      userRepositoryPort.bulkClearMentor(normalizedUserIds);
    } else {
      userRepositoryPort.bulkAssignMentor(normalizedUserIds, managerId);
    }
    return normalizedUserIds;
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
    if (value == null) {
      return null;
    }
    String trimmed = value.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }

  private void validateManagerAssignment(UserModel user, UserModel manager) {
    if (user == null || manager == null) {
      return;
    }

    if (user.getUserId().equals(manager.getUserId())) {
      throw new ConflictDataException("User cannot be assigned as their own manager");
    }

    Long cursorId = manager.getUserId();
    while (cursorId != null) {
      if (cursorId.equals(user.getUserId())) {
        throw new ConflictDataException("Manager assignment creates a cycle");
      }
      UserModel current = getUserOrThrow(cursorId);
      cursorId = current.getMentor() != null ? current.getMentor().getUserId() : null;
    }
  }
}
