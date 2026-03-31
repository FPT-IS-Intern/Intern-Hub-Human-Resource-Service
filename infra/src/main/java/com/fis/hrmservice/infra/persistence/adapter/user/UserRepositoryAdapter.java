package com.fis.hrmservice.infra.persistence.adapter.user;

import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.user.FilterUserCommand;
import com.fis.hrmservice.infra.mapper.UserMapper;
import com.fis.hrmservice.infra.persistence.entity.User;
import com.fis.hrmservice.infra.persistence.repository.user.UserJpaRepository;
import com.intern.hub.library.common.dto.PaginatedData;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

  private final UserJpaRepository userJpaRepository;

  private final UserMapper userMapper;

  @Override
  @Transactional
  public UserModel save(UserModel user) {
    User entity =
            userJpaRepository
                    .findById(user.getUserId())
                    .orElseThrow();

    userMapper.updateEntity(user, entity);

    User savedEntity = userJpaRepository.save(entity);

    return userMapper.toModel(savedEntity);
  }

  @Override
  @Transactional
  public UserModel create(UserModel user) {

    User entity = userMapper.toEntity(user);

    User saved = userJpaRepository.save(entity);

    return userMapper.toModel(saved);
  }

  @Override
  public List<UserModel> listMemberListBySupervisorId(Long supervisorId) {
    return userJpaRepository.listMemberListBySupervisorId(supervisorId).stream().map(userMapper::toModel).toList();
  }

  @Override
  public List<UserModel> findAllActiveUsers() {
    return userJpaRepository.findAllActiveUsers().stream().map(userMapper::toModel).toList();
  }

  @Override
  @Transactional
  public int assignMentor(Long userId, Long mentorId) {
    return userJpaRepository.assignMentor(userId, mentorId);
  }

  @Override
  @Transactional
  public int clearMentor(Long userId) {
    return userJpaRepository.clearMentor(userId);
  }

  @Override
  public List<Long> getAllUserId() {
      return userJpaRepository.findAll().stream().map(User::getId).toList();
  }

  @Override
  public Optional<UserModel> findById(Long userId) {
    return userJpaRepository.findById(userId).map(userMapper::toModel);
  }

  @Override
  public Optional<UserModel> findByEmail(String email) {
    return userJpaRepository.findByCompanyEmail(email).map(userMapper::toModel);
  }

  @Override
  public boolean existsByEmail(String email) {
    return userJpaRepository.existsByCompanyEmail(email);
  }

  @Override
  public boolean existsByIdNumber(String idNumber) {
    return userJpaRepository.existsByIdNumber(idNumber);
  }

  @Override
  public List<UserModel> findAll() {
    return userJpaRepository.findAll().stream().map(userMapper::toModel).toList();
  }

  @Override
  public PaginatedData<UserModel> filterUser(FilterUserCommand command, int page, int size) {

    Page<User> userPage = userJpaRepository.filterUser(command, PageRequest.of(page, size));

    List<UserModel> models = userMapper.toResponseList(userPage.getContent());

    return PaginatedData.<UserModel>builder()
        .items(models)
        .totalItems(userPage.getTotalElements())
        .totalPages(userPage.getTotalPages())
        .build();
  }

  @Override
  public int updateStatus(Long userId, UserStatus status) {
    return userJpaRepository.updateStatus(userId, status);
  }

  @Override
  public UserModel internalUserProfile(Long userId) {
    return userMapper.toModel(userJpaRepository.internalUserProfile(userId));
  }

  @Override
  public int suspendUser(Long userId, UserStatus status) {
    return userJpaRepository.suspendUser(userId, status);
  }

  @Override
  public int totalIntern() {
    return userJpaRepository.totalIntern();
  }

  @Override
  public int internshipChanging() {
    return userJpaRepository.internshipChanging();
  }

  @Override
  public List<UserModel> listAllSupervisor() {
    return userJpaRepository.listAllSupervisor().stream().map(userMapper::toModel).toList();
  }

  @Override
  public int updateIsFaceRegistry(Long userId, boolean isFaceRegistry) {
    return userJpaRepository.updateIsFaceRegistry(userId, isFaceRegistry);
  }

  @Override
  public List<UserModel> searchByQuery(String query) {
    return userJpaRepository.searchByQuery(query).stream().map(userMapper::toModel).toList();
  }

  @Override
  public Optional<UserModel> findOrgChartRoot() {
    return userJpaRepository.findOrgChartRoots(PageRequest.of(0, 1)).stream()
        .findFirst()
        .map(userMapper::toModel);
  }

  @Override
  public List<UserModel> findOrgChartRootCandidates(int limit) {
    int safeLimit = Math.max(limit, 1);
    return userJpaRepository.findOrgChartRoots(PageRequest.of(0, safeLimit)).stream()
        .map(userMapper::toModel)
        .toList();
  }

  @Override
  public PaginatedData<UserModel> findDirectSubordinates(Long managerId, int page, int size) {
    Page<User> userPage = userJpaRepository.findDirectSubordinates(managerId, PageRequest.of(page, size));
    return PaginatedData.<UserModel>builder()
        .items(userMapper.toResponseList(userPage.getContent()))
        .totalItems(userPage.getTotalElements())
        .totalPages(userPage.getTotalPages())
        .build();
  }

  @Override
  public long countDirectSubordinates(Long managerId) {
    return userJpaRepository.countByMentorId(managerId);
  }

  @Override
  public List<UserModel> findDirectSubordinatesLimited(Long managerId, int limit) {
    if (limit <= 0) {
      return List.of();
    }
    if (limit >= 20) {
      return userJpaRepository.findTop20ByMentorIdOrderByFullNameAsc(managerId).stream()
          .map(userMapper::toModel)
          .toList();
    }

    return userJpaRepository.findDirectSubordinates(managerId, PageRequest.of(0, limit)).stream()
        .map(userMapper::toModel)
        .toList();
  }

  @Override
  public PaginatedData<UserModel> searchOrgChartUsers(
      String query, String department, String status, int page, int size) {
    Page<User> userPage =
        userJpaRepository.searchOrgChartUsers(query, department, status, PageRequest.of(page, size));

    return PaginatedData.<UserModel>builder()
        .items(userMapper.toResponseList(userPage.getContent()))
        .totalItems(userPage.getTotalElements())
        .totalPages(userPage.getTotalPages())
        .build();
  }

  @Override
  public PaginatedData<UserModel> findAssignableOrgChartUsers(Long rootUserId, String query, int page, int size) {
    Page<User> userPage =
        userJpaRepository.findAssignableOrgChartUsers(rootUserId, query, PageRequest.of(page, size));

    return PaginatedData.<UserModel>builder()
        .items(userMapper.toResponseList(userPage.getContent()))
        .totalItems(userPage.getTotalElements())
        .totalPages(userPage.getTotalPages())
        .build();
  }

  @Override
  @Transactional
  public int bulkAssignMentor(List<Long> userIds, Long mentorId) {
    if (userIds == null || userIds.isEmpty()) {
      return 0;
    }
    return userJpaRepository.bulkAssignMentor(userIds, mentorId);
  }

  @Override
  @Transactional
  public int bulkClearMentor(List<Long> userIds) {
    if (userIds == null || userIds.isEmpty()) {
      return 0;
    }
    return userJpaRepository.bulkClearMentor(userIds);
  }

  @Override
  public Map<Long, Long> countDirectSubordinatesByManagerIds(List<Long> managerIds) {
    if (managerIds == null || managerIds.isEmpty()) {
      return Collections.emptyMap();
    }

    Map<Long, Long> result = new LinkedHashMap<>();
    for (Object[] row : userJpaRepository.countDirectSubordinatesByManagerIds(managerIds)) {
      result.put((Long) row[0], (Long) row[1]);
    }
    return result;
  }
}
