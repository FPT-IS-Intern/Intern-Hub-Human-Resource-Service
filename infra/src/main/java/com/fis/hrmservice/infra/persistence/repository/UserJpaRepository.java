package com.fis.hrmservice.infra.persistence.repository;

import com.fis.hrmservice.infra.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByCompanyEmail(String companyEmail);

    boolean existsByCompanyEmail(String companyEmail);

    boolean existsByIdNumber(String idNumber);

    User findMentorById(Long id);
}
