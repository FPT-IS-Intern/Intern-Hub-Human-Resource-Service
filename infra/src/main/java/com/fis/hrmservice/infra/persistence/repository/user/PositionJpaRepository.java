package com.fis.hrmservice.infra.persistence.repository.user;

import com.fis.hrmservice.infra.persistence.entity.Position;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** JPA Repository for PositionEntity. */
@Repository
public interface PositionJpaRepository extends JpaRepository<Position, Long> {

  Optional<Position> findByName(String name);

  @NullMarked
  Optional<Position> findById(Long id);

  @Query("SELECT p FROM Position p")
  List<Position> findAll();

  @Query(
      """
      SELECT p.id
      FROM Position p
      WHERE p.id IN :ids
      """)
  List<Long> findExistingPositionIds(@Param("ids") List<Long> ids);
}
