package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.orgchart.OrgChartNodeRepositoryPort;
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
  private static final int PARENT_CANDIDATE_FETCH_LIMIT = 500;

  UserRepositoryPort userRepositoryPort;
  OrgChartNodeRepositoryPort orgChartNodeRepositoryPort;

  public UserModel getRootUser(Long rootId) {
    if (rootId != null) {
      return orgChartNodeRepositoryPort
          .findUserInOrgChart(rootId)
          .orElseThrow(() -> new NotFoundException("Org chart root node not found with id: " + rootId));
    }

    return orgChartNodeRepositoryPort
        .findRootUser()
        .orElseThrow(() -> new NotFoundException("Org chart root user not found"));
  }

  public UserModel getUserOrThrow(Long userId) {
    return orgChartNodeRepositoryPort
        .findUserInOrgChart(userId)
        .orElseThrow(() -> new NotFoundException("Org chart node not found with id: " + userId));
  }

  private UserModel getRawUserOrThrow(Long userId) {
    return userRepositoryPort
        .findById(userId)
        .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
  }

  @Transactional
  public UserModel initializeRoot(Long userId) {
    orgChartNodeRepositoryPort.findRootUser().ifPresent(root -> {
          throw new ConflictDataException("Org chart root already exists with id: " + root.getUserId());
        });

    getRawUserOrThrow(userId);
    orgChartNodeRepositoryPort.initializeRoot(userId);
    return orgChartNodeRepositoryPort
        .findUserInOrgChart(userId)
        .orElseThrow(() -> new NotFoundException("Org chart root user not found with id: " + userId));
  }

  public List<UserModel> getDirectSubordinates(Long userId, int page, int limit) {
    return castItems(orgChartNodeRepositoryPort.findDirectSubordinates(userId, page, limit));
  }

  public PaginatedData<UserModel> getDirectSubordinatesPage(Long userId, int page, int limit) {
    return orgChartNodeRepositoryPort.findDirectSubordinates(userId, page, limit);
  }

  public List<UserModel> getPreviewSubordinates(Long userId, int limit) {
    return orgChartNodeRepositoryPort.findDirectSubordinatesLimited(userId, limit);
  }

  public PaginatedData<UserModel> searchUsers(
      String query, String department, String status, int page, int limit) {
    return orgChartNodeRepositoryPort.searchOrgChartUsers(
        normalize(query),
        normalize(department),
        normalize(status),
        page,
        limit);
  }

  public PaginatedData<UserModel> searchAssignableUsers(String query, int page, int limit) {
    return orgChartNodeRepositoryPort.findAssignableUsers(normalize(query), page, limit);
  }

  public PaginatedData<UserModel> searchParentCandidates(Long userId, String query, int page, int limit) {
    int safePage = Math.max(page, 0);
    int safeLimit = Math.max(limit, 1);

    getUserOrThrow(userId);

    PaginatedData<UserModel> searchPage =
        orgChartNodeRepositoryPort.searchOrgChartUsers(normalize(query), "", "", 0, PARENT_CANDIDATE_FETCH_LIMIT);

    List<UserModel> validCandidates =
        castItems(searchPage).stream()
            .filter(candidate -> isValidParentCandidate(userId, candidate.getUserId()))
            .toList();

    int fromIndex = Math.min(safePage * safeLimit, validCandidates.size());
    int toIndex = Math.min(fromIndex + safeLimit, validCandidates.size());
    List<UserModel> pagedCandidates = validCandidates.subList(fromIndex, toIndex);

    return PaginatedData.<UserModel>builder()
        .items(pagedCandidates)
        .totalItems((long) validCandidates.size())
        .totalPages(validCandidates.isEmpty() ? 0 : (int) Math.ceil((double) validCandidates.size() / safeLimit))
        .build();
  }

  public long countDirectSubordinates(Long userId) {
    return orgChartNodeRepositoryPort.countDirectSubordinates(userId);
  }

  public Map<Long, Long> countDirectSubordinates(List<Long> userIds) {
    return orgChartNodeRepositoryPort.countDirectSubordinatesByManagerIds(userIds);
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

    if (managerId == null) {
      for (Long userId : normalizedUserIds) {
        validateRemoval(userId);
      }
      orgChartNodeRepositoryPort.removeUsers(normalizedUserIds);
    } else {
      UserModel manager =
          orgChartNodeRepositoryPort
              .findUserInOrgChart(managerId)
              .orElseThrow(
                  () -> new NotFoundException("Manager node not found with id: " + managerId));
      for (Long userId : normalizedUserIds) {
        getRawUserOrThrow(userId);
        validateManagerAssignment(userId, manager.getUserId());
      }
      orgChartNodeRepositoryPort.assignUsersToManager(normalizedUserIds, managerId);
    }
    return normalizedUserIds;
  }

  public List<UserModel> getPathToRoot(Long userId) {
    LinkedList<UserModel> path = new LinkedList<>();
    Long currentUserId = userId;

    while (currentUserId != null) {
      UserModel currentUser = getUserOrThrow(currentUserId);
      path.addFirst(currentUser);
      currentUserId = orgChartNodeRepositoryPort.findParentUserId(currentUserId).orElse(null);
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
      return "";
    }
    String trimmed = value.trim();
    return trimmed.isEmpty() ? "" : trimmed;
  }

  private void validateManagerAssignment(Long userId, Long managerId) {
    if (userId == null || managerId == null) {
      return;
    }

    if (userId.equals(managerId)) {
      throw new ConflictDataException("User cannot be assigned as their own manager");
    }

    Long cursorId = managerId;
    while (cursorId != null) {
      if (cursorId.equals(userId)) {
        throw new ConflictDataException("Manager assignment creates a cycle");
      }
      cursorId = orgChartNodeRepositoryPort.findParentUserId(cursorId).orElse(null);
    }
  }

  private boolean isValidParentCandidate(Long userId, Long managerId) {
    if (userId == null || managerId == null) {
      return false;
    }

    if (userId.equals(managerId)) {
      return false;
    }

    Long cursorId = managerId;
    while (cursorId != null) {
      if (cursorId.equals(userId)) {
        return false;
      }
      cursorId = orgChartNodeRepositoryPort.findParentUserId(cursorId).orElse(null);
    }

    return true;
  }

  private void validateRemoval(Long userId) {
    if (orgChartNodeRepositoryPort.isRoot(userId)) {
      throw new ConflictDataException("Root node cannot be removed from org chart");
    }
    if (orgChartNodeRepositoryPort.existsChildren(userId)) {
      throw new ConflictDataException("Cannot remove org chart node that still has direct subordinates");
    }
  }
}
