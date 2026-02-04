package com.fis.hrmservice.infra.persistence.adapter.user;

import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.port.output.user.PositionRepositoryPort;
import com.fis.hrmservice.infra.persistence.entity.Position;
import com.fis.hrmservice.infra.persistence.repository.user.PositionJpaRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PositionRepositoryAdapter implements PositionRepositoryPort {

  @Autowired private PositionJpaRepository positionJpaRepository;

  @Override
  public Optional<PositionModel> findByCode(String positionCode) {
    return positionJpaRepository.findByName(positionCode).map(this::toModel);
  }

  @Override
  public Optional<PositionModel> findById(Long positionId) {
    return positionJpaRepository.findById(positionId).map(this::toModel);
  }

  private PositionModel toModel(Position entity) {
    return PositionModel.builder()
        .positionId(entity.getId())
        .name(entity.getName())
        .description(entity.getDescription())
        .build();
  }
}
