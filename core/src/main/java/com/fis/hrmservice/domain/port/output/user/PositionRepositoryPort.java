package com.fis.hrmservice.domain.port.output.user;

import com.fis.hrmservice.domain.model.user.PositionModel;
import java.util.List;
import java.util.Optional;

public interface PositionRepositoryPort {
  Optional<PositionModel> findByCode(String positionCode);

  Optional<PositionModel> findById(Long positionId);

  List<PositionModel> findAll();

  List<Long> findExistingPositionIds(List<Long> positionIds);
}
