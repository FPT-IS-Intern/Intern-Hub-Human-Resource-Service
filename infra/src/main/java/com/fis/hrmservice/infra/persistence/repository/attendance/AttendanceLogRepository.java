package com.fis.hrmservice.infra.persistence.repository.attendance;

import com.fis.hrmservice.infra.persistence.entity.AttendanceLog;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {
  Optional<AttendanceLog> findByUser_IdAndWorkDate(Long userId, LocalDate workDate);

  List<AttendanceLog> findAllByUser_IdAndWorkDateOrderByCheckInTimeDesc(Long userId, LocalDate workDate);

  List<AttendanceLog> findAllByWorkDateAndCheckOutTimeIsNullOrderByCheckInTimeAsc(LocalDate workDate);

  Optional<AttendanceLog> findFirstByUser_IdAndWorkDateAndCheckOutTimeIsNullOrderByCheckInTimeDesc(
      Long userId, LocalDate workDate);

  Optional<AttendanceLog> findFirstByUser_IdAndWorkDateOrderByCheckInTimeDesc(
      Long userId, LocalDate workDate);

  boolean existsByUser_IdAndWorkDateAndCheckInBranchId(Long userId, LocalDate workDate, UUID branchId);
}
