package com.fis.hrmservice.domain.port.output.ticket.remoteticket;

import com.fis.hrmservice.domain.model.user.WorkLocationModel;
import java.util.List;

public interface WorkLocationRepositoryPort {
  boolean existByLocationName(String locationName);

  List<String> findAllLocationNames();

  WorkLocationModel findByLocationName(String locationName);

  List<WorkLocationModel> findAll();

  WorkLocationModel findById(Long id);

  WorkLocationModel save(WorkLocationModel workLocation);

  WorkLocationModel create(WorkLocationModel workLocation);

  WorkLocationModel update(WorkLocationModel workLocation);

  int disableWorkLocation(Long id);

  int enableWorkLocation(Long id);
}
