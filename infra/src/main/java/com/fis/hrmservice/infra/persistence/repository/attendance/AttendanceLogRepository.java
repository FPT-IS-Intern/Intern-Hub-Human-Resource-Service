package com.fis.hrmservice.infra.persistence.repository.attendance;

import com.fis.hrmservice.infra.persistence.entity.AttendanceLog;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {
  Optional<AttendanceLog> findByUser_IdAndWorkDate(Long userId, LocalDate workDate);


  @Query("""
    SELECT al
    FROM AttendanceLog al
    JOIN FETCH al.user u
    LEFT JOIN FETCH u.department d
    WHERE
        (:nameOrKeyword IS NULL OR :nameOrKeyword = ''
         OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :nameOrKeyword, '%'))
         OR LOWER(u.companyEmail) LIKE LOWER(CONCAT('%', :nameOrKeyword, '%')))
        AND (:status IS NULL OR :status = '' OR al.attendanceStatus = :status)
    ORDER BY al.workDate DESC, al.checkInTime DESC
""")
  Page<AttendanceLog> filterAttendanceLogs(@Param("nameOrKeyword") String nameOrKeyword, @Param("status") String attendanceStatus, Pageable pageable);

  List<AttendanceLog> findAllByUser_IdAndWorkDateOrderByCheckInTimeDesc(Long userId, LocalDate workDate);

  List<AttendanceLog> findAllByWorkDateAndCheckOutTimeIsNullOrderByCheckInTimeAsc(LocalDate workDate);

  Optional<AttendanceLog> findFirstByUser_IdAndWorkDateAndCheckOutTimeIsNullOrderByCheckInTimeDesc(
      Long userId, LocalDate workDate);

  Optional<AttendanceLog> findFirstByUser_IdAndWorkDateOrderByCheckInTimeDesc(
      Long userId, LocalDate workDate);

  boolean existsByUser_IdAndWorkDateAndCheckInBranchId(Long userId, LocalDate workDate, UUID branchId);
}
