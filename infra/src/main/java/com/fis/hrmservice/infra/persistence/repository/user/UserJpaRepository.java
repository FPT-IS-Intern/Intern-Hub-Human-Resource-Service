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
                    :#{#command.positions} IS NULL
                    OR u.position.name IN :#{#command.positions}
                )
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
  Long updateStatus(Long id, UserStatus status);

  @Query("SELECT u FROM User u WHERE u.id = :userId AND u.sysStatus = 'APPROVED'")
  User internalUserProfile(@Param("userId") Long userId);

  @Transactional
  @Modifying
  @Query("UPDATE User u SET u.sysStatus = :suspend WHERE u.id = :userId")
  Long suspendUser(@Param("userId") Long userId, @Param("suspend") UserStatus status);

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

  @Query("SELECT u FROM User u WHERE u.position.name = 'STAFF'")
  List<User> listAllSupervisor();
}
