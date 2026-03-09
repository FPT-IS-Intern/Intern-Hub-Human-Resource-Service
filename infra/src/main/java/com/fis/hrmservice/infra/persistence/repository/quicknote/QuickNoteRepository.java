package com.fis.hrmservice.infra.persistence.repository.quicknote;

import com.fis.hrmservice.infra.persistence.entity.QuickNote;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuickNoteRepository extends JpaRepository<QuickNote, Long> {
  @Query("SELECT q FROM QuickNote q WHERE q.intern.id = :internId ORDER BY q.writeDate DESC")
  List<QuickNote> findAllByInternId(@Param("internId") Long internId);
}
