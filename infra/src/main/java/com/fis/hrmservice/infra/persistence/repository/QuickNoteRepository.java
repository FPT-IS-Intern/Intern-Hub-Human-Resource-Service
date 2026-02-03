package com.fis.hrmservice.infra.persistence.repository;

import com.fis.hrmservice.infra.persistence.entity.QuickNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuickNoteRepository extends JpaRepository<QuickNote, Long> {

}
