package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.response.OrgChartDepartmentResponse;
import com.fis.hrmservice.api.dto.response.OrgChartPageMetaResponse;
import com.fis.hrmservice.api.dto.response.OrgChartPagedResponse;
import com.fis.hrmservice.api.dto.response.OrgChartPathResponse;
import com.fis.hrmservice.api.dto.response.OrgChartUserDetailResponse;
import com.fis.hrmservice.api.dto.response.OrgChartUserLiteResponse;
import com.fis.hrmservice.api.dto.response.OrgChartUserNodeResponse;
import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.user.UserModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OrgChartApiMapper {

  public OrgChartUserNodeResponse toNodeResponse(
      UserModel user, long subordinateCount, List<OrgChartUserNodeResponse> children) {
    return OrgChartUserNodeResponse.builder()
        .id(user.getUserId())
        .name(user.getFullName())
        .title(getTitle(user))
        .department(user.getDepartment())
        .avatar(user.getAvatarUrl())
        .email(user.getCompanyEmail())
        .phone(user.getPhoneNumber())
        .status(getOrgChartStatus(user))
        .joinedDate(formatDate(user))
        .location(user.getAddress())
        .managerId(user.getMentor() != null ? user.getMentor().getUserId() : null)
        .hasChildren(subordinateCount > 0)
        .subordinateCount(subordinateCount)
        .children(children)
        .build();
  }

  public OrgChartUserDetailResponse toDetailResponse(
      UserModel user, UserModel manager, List<UserModel> subordinates, long subordinateCount) {
    return OrgChartUserDetailResponse.builder()
        .id(user.getUserId())
        .name(user.getFullName())
        .title(getTitle(user))
        .department(
            OrgChartDepartmentResponse.builder()
                .id(user.getDepartmentId())
                .name(user.getDepartment())
                .code(user.getDepartmentCode())
                .build())
        .avatar(user.getAvatarUrl())
        .email(user.getCompanyEmail())
        .phone(user.getPhoneNumber())
        .status(getOrgChartStatus(user))
        .joinedDate(formatDate(user))
        .location(user.getAddress())
        .manager(toLite(manager))
        .subordinates(subordinates.stream().map(this::toLite).toList())
        .projects(List.of())
        .hasChildren(subordinateCount > 0)
        .subordinateCount(subordinateCount)
        .build();
  }

  public OrgChartUserLiteResponse toLite(UserModel user) {
    if (user == null) {
      return null;
    }

    return OrgChartUserLiteResponse.builder()
        .id(user.getUserId())
        .name(user.getFullName())
        .title(getTitle(user))
        .avatar(user.getAvatarUrl())
        .build();
  }

  public OrgChartPathResponse toPathResponse(List<UserModel> users) {
    return OrgChartPathResponse.builder().data(users.stream().map(this::toLite).toList()).build();
  }

  public OrgChartPagedResponse<OrgChartUserNodeResponse> toPagedNodeResponse(
      List<UserModel> users, Map<Long, Long> subordinateCounts, int page, int limit, long total) {
    return OrgChartPagedResponse.<OrgChartUserNodeResponse>builder()
        .data(
            users.stream()
                .map(
                    user ->
                        toNodeResponse(
                            user,
                            subordinateCounts.getOrDefault(user.getUserId(), 0L),
                            List.of()))
                .toList())
        .meta(toMeta(page, limit, total))
        .build();
  }

  public OrgChartPagedResponse<OrgChartUserNodeResponse> toSearchResponse(
      List<UserModel> users, Map<Long, Long> subordinateCounts, int page, int limit, long total) {
    return toPagedNodeResponse(users, subordinateCounts, page, limit, total);
  }

  public OrgChartPageMetaResponse toMeta(int page, int limit, long total) {
    int totalPages = total == 0 ? 0 : (int) Math.ceil((double) total / limit);
    return OrgChartPageMetaResponse.builder()
        .total(total)
        .page(page)
        .limit(limit)
        .totalPages(totalPages)
        .build();
  }

  private String formatDate(UserModel user) {
    if (user.getInternshipStartDate() == null) {
      return null;
    }
    return user.getInternshipStartDate().toString();
  }

  private String getTitle(UserModel user) {
    return user.getPosition() != null ? user.getPosition().getName() : null;
  }

  private String getOrgChartStatus(UserModel user) {
    UserStatus status = user.getSysStatus();
    if (status == UserStatus.SUSPENDED || status == UserStatus.REJECTED) {
      return "inactive";
    }

    String positionName = user.getPosition() != null ? user.getPosition().getName() : null;
    if (positionName != null && positionName.toUpperCase().contains("INTERN")) {
      return "intern";
    }

    return "active";
  }
}
