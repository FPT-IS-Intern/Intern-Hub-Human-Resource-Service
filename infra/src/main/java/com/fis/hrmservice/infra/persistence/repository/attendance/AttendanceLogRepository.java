package com.fis.hrmservice.infra.persistence.repository.attendance;

import com.fis.hrmservice.infra.model.AttendanceInWeekResponse;
import com.fis.hrmservice.infra.persistence.entity.AttendanceLog;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {

  Optional<AttendanceLog> findByUser_IdAndWorkDate(Long userId, LocalDate workDate);

  @Query(value = """
        SELECT EXISTS (
            SELECT 1
            FROM attendance_logs
            WHERE user_id = :userId
            AND work_date = :workDate
        )
        """, nativeQuery = true)
  boolean existsByUserIdAndWorkDate(@Param("userId") Long userId,
                                    @Param("workDate") LocalDate workDate);

  /**
   * Pessimistic write lock — used when checking-in after ABSENT mark to prevent race conditions.
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT al FROM AttendanceLog al WHERE al.user.id = :userId AND al.workDate = :workDate")
  Optional<AttendanceLog> findByUserIdAndWorkDateForUpdate(
      @Param("userId") Long userId, @Param("workDate") LocalDate workDate);

  @Query(value = """
          SELECT al.*
          FROM attendance_logs al
          JOIN users u ON u.user_id = al.user_id
          LEFT JOIN departments d ON d.department_id = u.department_id
          WHERE
              (CAST(:nameOrKeyword AS text) IS NULL OR CAST(:nameOrKeyword AS text) = ''
               OR LOWER(u.full_name) LIKE LOWER('%' || CAST(:nameOrKeyword AS text) || '%')
               OR LOWER(u.company_email) LIKE LOWER('%' || CAST(:nameOrKeyword AS text) || '%'))
              AND (CAST(:status AS text) IS NULL OR al.attendance_status = CAST(:status AS text))
              AND (CAST(:startDate AS date) IS NULL OR al.work_date >= CAST(:startDate AS date))
              AND (CAST(:endDate AS date) IS NULL OR al.work_date <= CAST(:endDate AS date))
          ORDER BY al.work_date DESC, al.check_in_time DESC
          """,
          countQuery = """
          SELECT COUNT(*)
          FROM attendance_logs al
          JOIN users u ON u.user_id = al.user_id
          WHERE
              (CAST(:nameOrKeyword AS text) IS NULL OR CAST(:nameOrKeyword AS text) = ''
               OR LOWER(u.full_name) LIKE LOWER('%' || CAST(:nameOrKeyword AS text) || '%')
               OR LOWER(u.company_email) LIKE LOWER('%' || CAST(:nameOrKeyword AS text) || '%'))
              AND (CAST(:status AS text) IS NULL OR al.attendance_status = CAST(:status AS text))
              AND (CAST(:startDate AS date) IS NULL OR al.work_date >= CAST(:startDate AS date))
              AND (CAST(:endDate AS date) IS NULL OR al.work_date <= CAST(:endDate AS date))
          """,
          nativeQuery = true)
  Page<AttendanceLog> filterAttendanceLogs(
          @Param("nameOrKeyword") String nameOrKeyword,
          @Param("status") String status,
          @Param("startDate") LocalDate startDate,
          @Param("endDate") LocalDate endDate,
          Pageable pageable);

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

  List<AttendanceLog> findAllByWorkDateAndCheckInTimeIsNotNullAndCheckOutTimeIsNullOrderByCheckInTimeAsc(
      LocalDate workDate);

  Optional<AttendanceLog> findFirstByUser_IdAndWorkDateAndCheckInTimeIsNotNullAndCheckOutTimeIsNullOrderByCheckInTimeDesc(
      Long userId, LocalDate workDate);

  @Query("""
      SELECT al
      FROM AttendanceLog al
      WHERE al.user.id = :userId
        AND al.workDate = :workDate
      ORDER BY
        CASE WHEN al.checkInTime IS NULL THEN 1 ELSE 0 END,
        al.checkInTime DESC,
        al.id DESC
      """)
  List<AttendanceLog> findLatestRecordsByUserAndDate(
      @Param("userId") Long userId, @Param("workDate") LocalDate workDate);

  boolean existsByUser_IdAndWorkDateAndCheckInBranchId(Long userId, LocalDate workDate, UUID branchId);

  @Query(value = """
          SELECT COALESCE((COUNT(DISTINCT user_id) * 100) / NULLIF((SELECT COUNT(*) FROM users), 0), 0)
          FROM attendance_logs
          WHERE work_date BETWEEN :fromDate AND :toDate
            AND attendance_status = 'ABSENT'
          """, nativeQuery = true)
  Long getAbsentPercentage(@Param("fromDate") LocalDate fromDate,
                           @Param("toDate") LocalDate toDate);

  @Query(value = """
          SELECT COALESCE((COUNT(DISTINCT user_id) * 100) / NULLIF((SELECT COUNT(*) FROM users), 0), 0)
          FROM attendance_logs
          WHERE work_date BETWEEN :fromDate AND :toDate
            AND attendance_status = 'CHECK_IN_LATE'
          """, nativeQuery = true)
  Long getLatePercentage(@Param("fromDate") LocalDate fromDate,
                         @Param("toDate") LocalDate toDate);

  @Query(value = """
          SELECT COALESCE((COUNT(DISTINCT user_id) * 100) / NULLIF((SELECT COUNT(*) FROM users), 0), 0)
          FROM attendance_logs
          WHERE work_date BETWEEN :fromDate AND :toDate
            AND attendance_status = 'CHECK_IN_ON_TIME'
          """, nativeQuery = true)
  Long getOnTimePercentage(@Param("fromDate") LocalDate fromDate,
                           @Param("toDate") LocalDate toDate);

  @Query(value = """
      SELECT COUNT(*)
      FROM attendance_logs a
      WHERE (a.attendance_status LIKE 'CHECK_IN%'
         OR a.attendance_status LIKE 'CHECK_OUT%')
      AND a.user_id = :userId
      """,
          nativeQuery = true)
  int totalWorkDate(@Param("userId") Long userId);

  @Query("""
      SELECT COUNT(a)
      FROM AttendanceLog a
      WHERE a.attendanceStatus = 'CHECK_IN_LATE'
      AND a.user.id = :userId
      """)
  int totalLateTime(@Param("userId") Long userId);
}
