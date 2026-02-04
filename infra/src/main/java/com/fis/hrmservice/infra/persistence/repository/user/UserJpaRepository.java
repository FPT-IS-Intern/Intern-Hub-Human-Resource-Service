package com.fis.hrmservice.infra.persistence.repository.user;

import com.fis.hrmservice.domain.usecase.command.user.FilterUserCommand;
import com.fis.hrmservice.infra.persistence.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
            (
                :#{#command.keyword} IS NULL
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
  List<User> filterUser(@Param("command") FilterUserCommand command);
}
