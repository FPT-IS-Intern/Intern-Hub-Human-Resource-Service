package com.fis.hrmservice.infra.persistence.repository.user;

import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.usecase.command.user.FilterUserCommand;
import com.fis.hrmservice.infra.persistence.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

  Optional<User> findByCompanyEmail(String companyEmail);

  boolean existsByCompanyEmail(String companyEmail);

  boolean existsByIdNumber(String idNumber);

  User findMentorById(Long id);

  @Query("""
        SELECT u
        FROM User u
        WHERE u.sysStatus = 'APPROVED'
    """)
  List<User> findAllActiveUsers();

  @Query(
          """
          SELECT u FROM User u
          WHERE
              (:#{#command.keyword} IS NULL OR :#{#command.keyword} = ''
                  OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :#{#command.keyword}, '%'))
                  OR LOWER(u.companyEmail) LIKE LOWER(CONCAT('%', :#{#command.keyword}, '%'))
              )
          AND (
              :#{#command.sysStatuses} IS NULL
              OR u.sysStatus IN :#{#command.sysStatuses}
          )
          AND (
              :#{#command.roles} IS NULL
              OR UPPER(
                  CASE
                      WHEN LOCATE(' ', u.position.name) > 0
                          THEN SUBSTRING(u.position.name, 1, LOCATE(' ', u.position.name) - 1)
                      WHEN UPPER(u.position.name) LIKE 'INTERN%' AND LENGTH(u.position.name) > 6
                          THEN 'INTERN'
                      WHEN UPPER(u.position.name) LIKE 'STAFF%' AND LENGTH(u.position.name) > 5
                          THEN 'STAFF'
                      ELSE u.position.name
                  END
              ) IN :#{#command.roles}
          )
          AND (
              :#{#command.positions} IS NULL
              OR UPPER(u.position.name) IN :#{#command.positions}
              OR UPPER(
                  CASE
                      WHEN LOCATE(' ', u.position.name) > 0
                          THEN SUBSTRING(u.position.name, LOCATE(' ', u.position.name) + 1)
                      WHEN UPPER(u.position.name) LIKE 'INTERN%' AND LENGTH(u.position.name) > 6
                          THEN SUBSTRING(u.position.name, 7)
                      WHEN UPPER(u.position.name) LIKE 'STAFF%' AND LENGTH(u.position.name) > 5
                          THEN SUBSTRING(u.position.name, 6)
                      ELSE ''
                  END
              ) IN :#{#command.positions}
          )
          ORDER BY 
              CASE 
                  WHEN u.sysStatus = 'APPROVED' THEN 0
                  ELSE 1
              END,
              u.fullName ASC
          """)
  Page<User> filterUser(@Param("command") FilterUserCommand command, Pageable pageable);

  @Modifying
  @Transactional
  @Query(
      """
                    UPDATE User u
                    SET u.sysStatus = :status
                    WHERE u.id = :id
                    """)
  int updateStatus(@Param("id") Long id,@Param("status") UserStatus status);

  @Query("SELECT u FROM User u WHERE u.id = :userId")
  User internalUserProfile(@Param("userId") Long userId);

  @Transactional
  @Modifying
  @Query("UPDATE User u SET u.sysStatus = :suspend WHERE u.id = :userId")
  int suspendUser(@Param("userId") Long userId, @Param("suspend") UserStatus status);

  @Query("SELECT COUNT(u) FROM User u WHERE u.sysStatus = 'APPROVED'")
  int totalIntern();

  @Query(
          value =
                  """
                          SELECT
                              COUNT(CASE
                                  WHEN date_trunc('month', u.internship_start_date) = date_trunc('month', CURRENT_DATE)
                                  THEN 1
                              END)
                              -
                              COUNT(CASE
                                  WHEN date_trunc('month', u.internship_start_date) = date_trunc('month', CURRENT_DATE - INTERVAL '1 month')
                                  THEN 1
                              END)
                          FROM users u
                          JOIN positions p ON u.position_id = p.position_id
                          WHERE u.internship_start_date IS NOT NULL
                            AND LOWER(p.name) LIKE '%intern%'
                          """,
          nativeQuery = true)
  int internshipChanging();

  @Query("SELECT u FROM User u JOIN u.position p WHERE LOWER(p.name) LIKE '%staff%'")
  List<User> listAllSupervisor();

  @Modifying
  @Transactional
  @Query("UPDATE User u SET u.isFaceRegistry = :isFaceRegistry WHERE u.id = :userId")
  int updateIsFaceRegistry(@Param("userId") Long userId, @Param("isFaceRegistry") boolean isFaceRegistry);

  @Query("SELECT u FROM User u WHERE u.mentor.id = :supervisorId")
  List<User> listMemberListBySupervisorId(@Param("supervisorId") Long supervisorId);

  @Modifying
  @Transactional
  @Query("UPDATE User u SET u.mentor.id = :mentorId WHERE u.id = :userId")
  int assignMentor(@Param("userId") Long userId, @Param("mentorId") Long mentorId);

  @Modifying
  @Transactional
  @Query("UPDATE User u SET u.mentor = null WHERE u.id = :userId")
  int clearMentor(@Param("userId") Long userId);

  @Modifying
  @Transactional
  @Query("UPDATE User u SET u.mentor.id = :mentorId WHERE u.id IN :userIds")
  int bulkAssignMentor(@Param("userIds") List<Long> userIds, @Param("mentorId") Long mentorId);

  @Modifying
  @Transactional
  @Query("UPDATE User u SET u.mentor = null WHERE u.id IN :userIds")
  int bulkClearMentor(@Param("userIds") List<Long> userIds);

  @Query("""
      SELECT u FROM User u
      WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
         OR LOWER(u.companyEmail) LIKE LOWER(CONCAT('%', :query, '%'))
      ORDER BY u.fullName ASC
      """)
  List<User> searchByQuery(@Param("query") String query);

  @Query("""
      SELECT u FROM User u
      WHERE u.mentor IS NULL
      ORDER BY
        CASE WHEN u.sysStatus = 'APPROVED' THEN 0 ELSE 1 END,
        u.id ASC
      """)
  Page<User> findOrgChartRoots(Pageable pageable);

  @Query("""
      SELECT u FROM User u
      WHERE u.mentor.id = :managerId
      ORDER BY u.fullName ASC
      """)
  Page<User> findDirectSubordinates(@Param("managerId") Long managerId, Pageable pageable);

  long countByMentorId(Long mentorId);

  List<User> findTop20ByMentorIdOrderByFullNameAsc(Long mentorId);

  @Query("""
      SELECT u FROM User u
      WHERE (:query IS NULL OR :query = ''
              OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
              OR LOWER(u.companyEmail) LIKE LOWER(CONCAT('%', :query, '%'))
              OR LOWER(COALESCE(u.position.name, '')) LIKE LOWER(CONCAT('%', :query, '%')))
        AND (:department IS NULL OR :department = ''
              OR LOWER(COALESCE(u.department.name, '')) LIKE LOWER(CONCAT('%', :department, '%'))
              OR function('str', u.department.id) = :department)
        AND (:status IS NULL OR :status = ''
              OR (:status = 'active' AND u.sysStatus = 'APPROVED' AND UPPER(COALESCE(u.position.name, '')) NOT LIKE '%INTERN%')
              OR (:status = 'intern' AND u.sysStatus = 'APPROVED' AND UPPER(COALESCE(u.position.name, '')) LIKE '%INTERN%')
              OR (:status = 'inactive' AND u.sysStatus IN ('REJECTED', 'SUSPENDED')))
      ORDER BY u.fullName ASC
      """)
  Page<User> searchOrgChartUsers(
      @Param("query") String query,
      @Param("department") String department,
      @Param("status") String status,
      Pageable pageable);

  @Query("""
      SELECT u FROM User u
      WHERE u.mentor IS NULL
        AND (:rootUserId IS NULL OR u.id <> :rootUserId)
        AND (:query IS NULL OR :query = ''
              OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
              OR LOWER(u.companyEmail) LIKE LOWER(CONCAT('%', :query, '%'))
              OR LOWER(COALESCE(u.position.name, '')) LIKE LOWER(CONCAT('%', :query, '%')))
      ORDER BY u.fullName ASC
      """)
  Page<User> findAssignableOrgChartUsers(
      @Param("rootUserId") Long rootUserId,
      @Param("query") String query,
      Pageable pageable);

  @Query("""
      SELECT u.mentor.id, COUNT(u)
      FROM User u
      WHERE u.mentor.id IN :managerIds
      GROUP BY u.mentor.id
      """)
  List<Object[]> countDirectSubordinatesByManagerIds(@Param("managerIds") List<Long> managerIds);
}
