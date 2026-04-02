package com.fis.hrmservice.infra.persistence.repository.orgchart;

import com.fis.hrmservice.infra.persistence.entity.OrgChartNode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgChartNodeJpaRepository extends JpaRepository<OrgChartNode, Long> {

  @EntityGraph(attributePaths = {"user", "user.position", "user.department"})
  @Query("SELECT n FROM OrgChartNode n WHERE n.isRoot = true")
  Optional<OrgChartNode> findRootNode();

  boolean existsByIsRootTrue();

  @EntityGraph(attributePaths = {"user", "user.position", "user.department", "parentUser"})
  @Query("SELECT n FROM OrgChartNode n WHERE n.user.id = :userId")
  Optional<OrgChartNode> findByUserId(@Param("userId") Long userId);

  @EntityGraph(attributePaths = {"user", "user.position", "user.department", "parentUser"})
  @Query("SELECT n FROM OrgChartNode n WHERE n.user.id IN :userIds")
  List<OrgChartNode> findByUserIds(@Param("userIds") List<Long> userIds);

  @EntityGraph(attributePaths = {"user", "user.position", "user.department", "parentUser"})
  @Query(
      value = """
      SELECT n FROM OrgChartNode n
      WHERE n.parentUser.id = :managerId
      ORDER BY n.user.fullName ASC
      """,
      countQuery = """
      SELECT COUNT(n) FROM OrgChartNode n
      WHERE n.parentUser.id = :managerId
      """)
  Page<OrgChartNode> findDirectSubordinates(@Param("managerId") Long managerId, Pageable pageable);

  long countByParentUser_Id(Long managerId);

  boolean existsByParentUser_Id(Long parentUserId);

  @Query("SELECT n.parentUser.id FROM OrgChartNode n WHERE n.user.id = :userId")
  Optional<Long> findParentUserId(@Param("userId") Long userId);

  @Query("SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END FROM OrgChartNode n WHERE n.user.id = :userId AND n.isRoot = true")
  boolean isRoot(@Param("userId") Long userId);

  @EntityGraph(attributePaths = {"user", "user.position", "user.department", "parentUser"})
  @Query(
      value = """
      SELECT n FROM OrgChartNode n
      JOIN n.user u
      LEFT JOIN u.position p
      LEFT JOIN u.department d
      WHERE (:query IS NULL OR :query = ''
              OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
              OR LOWER(u.companyEmail) LIKE LOWER(CONCAT('%', :query, '%'))
              OR LOWER(COALESCE(p.name, '')) LIKE LOWER(CONCAT('%', :query, '%')))
        AND (:department IS NULL OR :department = ''
              OR LOWER(COALESCE(d.name, '')) LIKE LOWER(CONCAT('%', :department, '%'))
              OR function('str', d.id) = :department)
        AND (:status IS NULL OR :status = ''
              OR (:status = 'active' AND u.sysStatus = 'APPROVED' AND UPPER(COALESCE(p.name, '')) NOT LIKE '%INTERN%')
              OR (:status = 'intern' AND u.sysStatus = 'APPROVED' AND UPPER(COALESCE(p.name, '')) LIKE '%INTERN%')
              OR (:status = 'inactive' AND u.sysStatus IN ('REJECTED', 'SUSPENDED')))
      ORDER BY u.fullName ASC
      """,
      countQuery = """
      SELECT COUNT(n) FROM OrgChartNode n
      JOIN n.user u
      LEFT JOIN u.position p
      LEFT JOIN u.department d
      WHERE (:query IS NULL OR :query = ''
              OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
              OR LOWER(u.companyEmail) LIKE LOWER(CONCAT('%', :query, '%'))
              OR LOWER(COALESCE(p.name, '')) LIKE LOWER(CONCAT('%', :query, '%')))
        AND (:department IS NULL OR :department = ''
              OR LOWER(COALESCE(d.name, '')) LIKE LOWER(CONCAT('%', :department, '%'))
              OR function('str', d.id) = :department)
        AND (:status IS NULL OR :status = ''
              OR (:status = 'active' AND u.sysStatus = 'APPROVED' AND UPPER(COALESCE(p.name, '')) NOT LIKE '%INTERN%')
              OR (:status = 'intern' AND u.sysStatus = 'APPROVED' AND UPPER(COALESCE(p.name, '')) LIKE '%INTERN%')
              OR (:status = 'inactive' AND u.sysStatus IN ('REJECTED', 'SUSPENDED')))
      """)
  Page<OrgChartNode> searchOrgChartUsers(
      @Param("query") String query,
      @Param("department") String department,
      @Param("status") String status,
      Pageable pageable);

  @Query("""
      SELECT n.parentUser.id, COUNT(n)
      FROM OrgChartNode n
      WHERE n.parentUser.id IN :managerIds
      GROUP BY n.parentUser.id
      """)
  List<Object[]> countDirectSubordinatesByManagerIds(@Param("managerIds") List<Long> managerIds);

  default Map<Long, Long> toCountMap(List<Long> managerIds) {
    if (managerIds == null || managerIds.isEmpty()) {
      return Map.of();
    }
    return countDirectSubordinatesByManagerIds(managerIds).stream()
        .collect(Collectors.toMap(row -> (Long) row[0], row -> (Long) row[1]));
  }
}
