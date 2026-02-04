package com.fis.hrmservice.infra.persistence.repository.user;

import com.fis.hrmservice.infra.persistence.entity.Position;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** JPA Repository for PositionEntity. */
@Repository
public interface PositionJpaRepository extends JpaRepository<Position, Long> {

  Optional<Position> findByName(String name);

  @NullMarked
  Optional<Position> findById(Long id);
}
