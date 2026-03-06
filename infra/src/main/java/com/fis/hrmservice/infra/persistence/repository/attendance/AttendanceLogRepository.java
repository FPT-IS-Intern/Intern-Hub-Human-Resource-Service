package com.fis.hrmservice.infra.persistence.repository.attendance;

import com.fis.hrmservice.infra.model.AttendanceInWeekResponse;
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

  @Query(value = """
          SELECT  
              TO_CHAR(d.day, 'DY') AS dayInWeek,
              CASE  
                  WHEN a.attendance_id IS NOT NULL THEN 'CHECK'
                  ELSE 'ABSENT'
              END AS status
          FROM generate_series(
                  DATE_TRUNC('week', CURRENT_DATE)::date,
                  (DATE_TRUNC('week', CURRENT_DATE) + INTERVAL '4 days')::date,
                  INTERVAL '1 day'
               ) AS d(day)
          LEFT JOIN attendance_logs a
                 ON a.work_date = d.day
                AND a.user_id = :userId
          ORDER BY d.day
          """, nativeQuery = true)
  List<AttendanceInWeekResponse> getAttendanceInWeek(@Param("userId") Long userId);

  List<AttendanceLog> findAllByUser_IdAndWorkDateOrderByCheckInTimeDesc(Long userId, LocalDate workDate);

  List<AttendanceLog> findAllByWorkDateAndCheckOutTimeIsNullOrderByCheckInTimeAsc(LocalDate workDate);

  Optional<AttendanceLog> findFirstByUser_IdAndWorkDateAndCheckOutTimeIsNullOrderByCheckInTimeDesc(
      Long userId, LocalDate workDate);

  Optional<AttendanceLog> findFirstByUser_IdAndWorkDateOrderByCheckInTimeDesc(
      Long userId, LocalDate workDate);

  boolean existsByUser_IdAndWorkDateAndCheckInBranchId(Long userId, LocalDate workDate, UUID branchId);
}
