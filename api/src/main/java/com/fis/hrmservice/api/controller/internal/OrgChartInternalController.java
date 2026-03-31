package com.fis.hrmservice.api.controller.internal;

import com.fis.hrmservice.api.dto.request.OrgChartBulkManagerUpdateRequest;
import com.fis.hrmservice.api.dto.request.OrgChartInitializeRootRequest;
import com.fis.hrmservice.api.dto.response.OrgChartBulkManagerUpdateResponse;
import com.fis.hrmservice.api.dto.response.OrgChartPagedResponse;
import com.fis.hrmservice.api.dto.response.OrgChartPathResponse;
import com.fis.hrmservice.api.dto.response.OrgChartUserDetailResponse;
import com.fis.hrmservice.api.dto.response.OrgChartUserNodeResponse;
import com.fis.hrmservice.api.mapper.OrgChartApiMapper;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.implement.user.OrgChartUseCaseImpl;
import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.Internal;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hrm/internal/orgchart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrgChartInternalController {

  OrgChartUseCaseImpl orgChartUseCase;
  OrgChartApiMapper orgChartApiMapper;

  @GetMapping
  @Internal
  public ResponseApi<OrgChartUserNodeResponse> getOrgChart(
      @RequestParam(required = false) Long rootId,
      @RequestParam(defaultValue = "1") int maxDepth,
      @RequestParam(required = false) String fields) {
    UserModel rootUser = orgChartUseCase.getRootUser(rootId);
    UserModel tree = orgChartUseCase.buildTree(rootUser, maxDepth);
    return ResponseApi.ok(toTreeResponse(tree));
  }

  @GetMapping("/users/{userId}/subordinates")
  @Internal
  public ResponseApi<OrgChartPagedResponse<OrgChartUserNodeResponse>> getSubordinates(
      @PathVariable Long userId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "50") int limit,
      @RequestParam(required = false) String fields) {
    int safePage = Math.max(page, 1);
    int safeLimit = Math.max(limit, 1);
    PaginatedData<UserModel> subordinatesPage =
        orgChartUseCase.getDirectSubordinatesPage(userId, safePage - 1, safeLimit);
    List<UserModel> users = getItems(subordinatesPage);
    Map<Long, Long> counts =
        orgChartUseCase.countDirectSubordinates(users.stream().map(UserModel::getUserId).toList());

    return ResponseApi.ok(
        orgChartApiMapper.toPagedNodeResponse(
            users, counts, safePage, safeLimit, subordinatesPage.getTotalItems()));
  }

  @GetMapping("/users/{userId}")
  @Internal
  public ResponseApi<OrgChartUserDetailResponse> getUserDetail(@PathVariable Long userId) {
    return ResponseApi.ok(toDetailResponse(orgChartUseCase.getUserOrThrow(userId)));
  }

  @PostMapping("/root")
  @Internal
  public ResponseApi<OrgChartUserDetailResponse> initializeRoot(
      @RequestBody OrgChartInitializeRootRequest request) {
    return ResponseApi.ok(toDetailResponse(orgChartUseCase.initializeRoot(request.getUserId())));
  }

  @GetMapping("/users")
  @Internal
  public ResponseApi<OrgChartPagedResponse<OrgChartUserNodeResponse>> searchUsers(
      @RequestParam(required = false, name = "q") String query,
      @RequestParam(required = false) String department,
      @RequestParam(required = false) String status,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int limit,
      @RequestParam(required = false) String fields) {
    int safePage = Math.max(page, 1);
    int safeLimit = Math.max(limit, 1);
    PaginatedData<UserModel> searchPage =
        orgChartUseCase.searchUsers(query, department, status, safePage - 1, safeLimit);
    List<UserModel> users = getItems(searchPage);
    Map<Long, Long> counts =
        orgChartUseCase.countDirectSubordinates(users.stream().map(UserModel::getUserId).toList());

    return ResponseApi.ok(
        orgChartApiMapper.toSearchResponse(
            users, counts, safePage, safeLimit, searchPage.getTotalItems()));
  }

  @GetMapping("/assignable-users")
  @Internal
  public ResponseApi<OrgChartPagedResponse<com.fis.hrmservice.api.dto.response.OrgChartUserLiteResponse>> getAssignableUsers(
      @RequestParam(required = false, name = "q") String query,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int limit) {
    int safePage = Math.max(page, 1);
    int safeLimit = Math.max(limit, 1);
    PaginatedData<UserModel> assignablePage =
        orgChartUseCase.searchAssignableUsers(query, safePage - 1, safeLimit);
    List<UserModel> users = getItems(assignablePage);

    return ResponseApi.ok(
        OrgChartPagedResponse.<com.fis.hrmservice.api.dto.response.OrgChartUserLiteResponse>builder()
            .data(users.stream().map(orgChartApiMapper::toLite).toList())
            .meta(orgChartApiMapper.toMeta(safePage, safeLimit, assignablePage.getTotalItems()))
            .build());
  }

  @PutMapping("/users/manager")
  @Internal
  public ResponseApi<OrgChartBulkManagerUpdateResponse> bulkUpdateManager(
      @RequestBody OrgChartBulkManagerUpdateRequest request) {
    List<Long> updatedUserIds = orgChartUseCase.bulkUpdateManager(request.getUserIds(), request.getManagerId());
    return ResponseApi.ok(
        OrgChartBulkManagerUpdateResponse.builder()
            .updatedUserIds(updatedUserIds)
            .managerId(request.getManagerId())
            .updatedCount(updatedUserIds.size())
            .build());
  }

  @GetMapping("/users/{userId}/path")
  @Internal
  public ResponseApi<OrgChartPathResponse> getPathToRoot(@PathVariable Long userId) {
    return ResponseApi.ok(orgChartApiMapper.toPathResponse(orgChartUseCase.getPathToRoot(userId)));
  }

  private OrgChartUserNodeResponse toTreeResponse(UserModel user) {
    long subordinateCount = orgChartUseCase.countDirectSubordinates(user.getUserId());
    List<OrgChartUserNodeResponse> children =
        user.getChildren().stream().map(this::toTreeResponse).toList();
    return orgChartApiMapper.toNodeResponse(user, subordinateCount, children);
  }

  private OrgChartUserDetailResponse toDetailResponse(UserModel user) {
    UserModel manager =
        user.getMentor() != null ? orgChartUseCase.getUserOrThrow(user.getMentor().getUserId()) : null;
    long subordinateCount = orgChartUseCase.countDirectSubordinates(user.getUserId());
    List<UserModel> previewSubordinates = orgChartUseCase.getPreviewSubordinates(user.getUserId(), 20);
    return orgChartApiMapper.toDetailResponse(user, manager, previewSubordinates, subordinateCount);
  }

  @SuppressWarnings("unchecked")
  private List<UserModel> getItems(PaginatedData<UserModel> paginatedData) {
    return (List<UserModel>) paginatedData.getItems();
  }
}
