package com.fis.hrmservice.infra.persistence.adapter.user;

import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.port.output.user.PositionRepositoryPort;
import com.fis.hrmservice.infra.persistence.entity.Position;
import com.fis.hrmservice.infra.persistence.repository.user.PositionJpaRepository;

import java.util.List;
import java.util.Optional;

import com.intern.hub.library.common.utils.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PositionRepositoryAdapter implements PositionRepositoryPort {

    private final PositionJpaRepository positionJpaRepository;
    private final Snowflake snowflake;

    @Override
    public Optional<PositionModel> findByCode(String positionCode) {
        return positionJpaRepository.findByName(positionCode).map(this::toModel);
    }

    @Override
    public Optional<PositionModel> findById(Long positionId) {
        return positionJpaRepository.findById(positionId).map(this::toModel);
    }

    @Override
    public List<PositionModel> findAll() {
        return positionJpaRepository.findAll().stream().map(this::toModel).toList();
    }

    @Override
    public List<Long> findExistingPositionIds(List<Long> positionIds) {
        if (positionIds == null || positionIds.isEmpty()) {
            return List.of();
        }
        return positionJpaRepository.findExistingPositionIds(positionIds);
    }

    @Override
    public PositionModel addPosition(PositionModel position) {
        return toModel(positionJpaRepository.save(Position.builder()
                .id(snowflake.next())
                .name(position.getName())
                .description(position.getDescription())
                .build()));
    }

    private PositionModel toModel(Position entity) {
        return PositionModel.builder()
                .positionId(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}
