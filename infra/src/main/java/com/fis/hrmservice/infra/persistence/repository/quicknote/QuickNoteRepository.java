package com.fis.hrmservice.infra.persistence.repository.quicknote;

import com.fis.hrmservice.infra.persistence.entity.QuickNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuickNoteRepository extends JpaRepository<QuickNote, Long> {
    List<QuickNote> findAllByInternId(Long internId);
}
