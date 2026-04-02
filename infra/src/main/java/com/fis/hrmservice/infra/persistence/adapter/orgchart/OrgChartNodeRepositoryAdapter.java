package com.fis.hrmservice.infra.persistence.adapter.orgchart;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.port.output.orgchart.OrgChartNodeRepositoryPort;
import com.fis.hrmservice.infra.mapper.UserMapper;
import com.fis.hrmservice.infra.persistence.entity.OrgChartNode;
import com.fis.hrmservice.infra.persistence.repository.orgchart.OrgChartNodeJpaRepository;
import com.fis.hrmservice.infra.persistence.repository.user.UserJpaRepository;
import com.intern.hub.library.common.dto.PaginatedData;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class OrgChartNodeRepositoryAdapter implements OrgChartNodeRepositoryPort {

  private final OrgChartNodeJpaRepository orgChartNodeJpaRepository;
  private final UserJpaRepository userJpaRepository;
  private final UserMapper userMapper;

  public OrgChartNodeRepositoryAdapter(
      OrgChartNodeJpaRepository orgChartNodeJpaRepository,
      UserJpaRepository userJpaRepository,
      UserMapper userMapper) {
    this.orgChartNodeJpaRepository = orgChartNodeJpaRepository;
    this.userJpaRepository = userJpaRepository;
    this.userMapper = userMapper;
  }

  @Override
  public Optional<UserModel> findRootUser() {
    return orgChartNodeJpaRepository.findRootNode().map(this::toUserModel);
  }

  @Override
  public Optional<UserModel> findUserInOrgChart(Long userId) {
    return orgChartNodeJpaRepository.findByUserId(userId).map(this::toUserModel);
  }

  @Override
  public Optional<Long> findParentUserId(Long userId) {
    return orgChartNodeJpaRepository.findParentUserId(userId);
  }

  @Override
  @Transactional
  public void initializeRoot(Long userId) {
    // Keep single-root invariant: if another root exists, demote it first.
    orgChartNodeJpaRepository.findRootNode().ifPresent(existingRoot -> {
      Long existingRootUserId = existingRoot.getUser() != null ? existingRoot.getUser().getId() : null;
      if (existingRootUserId != null && !existingRootUserId.equals(userId)) {
        existingRoot.setIsRoot(Boolean.FALSE);
        existingRoot.setParentUser(userJpaRepository.getReferenceById(userId));
        orgChartNodeJpaRepository.save(existingRoot);
      }
    });

    OrgChartNode node = orgChartNodeJpaRepository.findByUserId(userId).orElseGet(OrgChartNode::new);
    node.setUser(userJpaRepository.getReferenceById(userId));
    node.setParentUser(null);
    node.setIsRoot(Boolean.TRUE);
    orgChartNodeJpaRepository.save(node);
  }

  @Override
  public PaginatedData<UserModel> findDirectSubordinates(Long managerId, int page, int size) {
    Page<OrgChartNode> nodes =
        orgChartNodeJpaRepository.findDirectSubordinates(managerId, PageRequest.of(page, size));
    return PaginatedData.<UserModel>builder()
        .items(nodes.getContent().stream().map(this::toUserModel).toList())
        .totalItems(nodes.getTotalElements())
        .totalPages(nodes.getTotalPages())
        .build();
  }

  @Override
  public long countDirectSubordinates(Long managerId) {
    return orgChartNodeJpaRepository.countByParentUser_Id(managerId);
  }

  @Override
  public List<UserModel> findDirectSubordinatesLimited(Long managerId, int limit) {
    if (limit <= 0) {
      return List.of();
    }

    return orgChartNodeJpaRepository.findDirectSubordinates(managerId, PageRequest.of(0, limit)).stream()
        .map(this::toUserModel)
        .toList();
  }

  @Override
  public PaginatedData<UserModel> searchOrgChartUsers(
      String query, String department, String status, int page, int size) {
    Page<OrgChartNode> nodes =
        orgChartNodeJpaRepository.searchOrgChartUsers(query, department, status, PageRequest.of(page, size));
    return PaginatedData.<UserModel>builder()
        .items(nodes.getContent().stream().map(this::toUserModel).toList())
        .totalItems(nodes.getTotalElements())
        .totalPages(nodes.getTotalPages())
        .build();
  }

  @Override
  public PaginatedData<UserModel> findAssignableUsers(String query, int page, int size) {
    Page<com.fis.hrmservice.infra.persistence.entity.User> users =
        userJpaRepository.findAssignableOrgChartUsers(null, query, PageRequest.of(page, size));

    return PaginatedData.<UserModel>builder()
        .items(userMapper.toResponseList(users.getContent()))
        .totalItems(users.getTotalElements())
        .totalPages(users.getTotalPages())
        .build();
  }

  @Override
  @Transactional
  public void assignUsersToManager(List<Long> userIds, Long managerId) {
    if (userIds == null || userIds.isEmpty()) {
      return;
    }

    Map<Long, OrgChartNode> existingNodes = new LinkedHashMap<>();
    for (OrgChartNode node : orgChartNodeJpaRepository.findByUserIds(userIds)) {
      existingNodes.put(node.getUser().getId(), node);
    }

    com.fis.hrmservice.infra.persistence.entity.User parentRef =
        managerId != null ? userJpaRepository.getReferenceById(managerId) : null;

    List<OrgChartNode> nodesToSave = userIds.stream().map(userId -> {
      OrgChartNode node = existingNodes.getOrDefault(userId, new OrgChartNode());
      if (node.getUser() == null) {
        node.setUser(userJpaRepository.getReferenceById(userId));
      }
      node.setParentUser(parentRef);
      node.setIsRoot(Boolean.FALSE);
      return node;
    }).toList();

    orgChartNodeJpaRepository.saveAll(nodesToSave);
  }

  @Override
  @Transactional
  public void removeUsers(List<Long> userIds) {
    if (userIds == null || userIds.isEmpty()) {
      return;
    }

    List<OrgChartNode> nodes = orgChartNodeJpaRepository.findByUserIds(userIds);
    orgChartNodeJpaRepository.deleteAll(nodes);
  }

  @Override
  public boolean existsChildren(Long userId) {
    return orgChartNodeJpaRepository.existsByParentUser_Id(userId);
  }

  @Override
  public boolean isRoot(Long userId) {
    return orgChartNodeJpaRepository.isRoot(userId);
  }

  @Override
  public Map<Long, Long> countDirectSubordinatesByManagerIds(List<Long> managerIds) {
    return orgChartNodeJpaRepository.toCountMap(managerIds);
  }

  private UserModel toUserModel(OrgChartNode node) {
    com.fis.hrmservice.infra.persistence.entity.User user = node.getUser();
    UserModel userModel =
        UserModel.builder()
            .userId(user.getId())
            .fullName(user.getFullName())
            .companyEmail(user.getCompanyEmail())
            .phoneNumber(user.getPhoneNumber())
            .idNumber(user.getIdNumber())
            .dateOfBirth(user.getDateOfBirth())
            .address(user.getAddress())
            .sysStatus(user.getSysStatus())
            .internshipStartDate(user.getInternshipStartDate())
            .internshipEndDate(user.getInternshipEndDate())
            .position(
                user.getPosition() != null
                    ? PositionModel.builder()
                        .positionId(user.getPosition().getId())
                        .name(user.getPosition().getName())
                        .description(user.getPosition().getDescription())
                        .build()
                    : null)
            .department(user.getDepartment() != null ? user.getDepartment().getName() : null)
            .departmentId(user.getDepartment() != null ? user.getDepartment().getId() : null)
            .departmentCode(user.getDepartment() != null ? user.getDepartment().getCode() : null)
            .isFaceRegistry(user.getIsFaceRegistry())
            .avatarUrl(user.getAvatarUrl())
            .cvUrl(user.getCvUrl())
            .children(List.of())
            .build();
    if (node.getParentUser() != null) {
      userModel.setMentor(
          UserModel.builder()
              .userId(node.getParentUser().getId())
              .fullName(node.getParentUser().getFullName())
              .build());
    } else {
      userModel.setMentor(null);
    }
    return userModel;
  }
}
