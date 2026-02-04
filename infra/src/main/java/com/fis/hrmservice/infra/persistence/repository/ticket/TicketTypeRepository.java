package com.fis.hrmservice.infra.persistence.repository.ticket;

import com.fis.hrmservice.infra.persistence.entity.TicketType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
  @Query("SELECT t.typeName FROM TicketType t")
  List<String> findTypeNames();

  TicketType findByTypeName(String typeName);
}
