package com.fis.hrmservice.infra.persistence.repository.ticket;

import java.util.List;

import com.fis.hrmservice.infra.persistence.entity.WorkLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkLocationRepository extends JpaRepository<WorkLocation, Long> {

  @Query("SELECT w.name FROM WorkLocation w")
  List<String> getAllWorkLocationName();

  @Query(
      "SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM WorkLocation w WHERE w.name = :locationName")
  boolean existsByName(@Param("locationName") String locationName);
}
