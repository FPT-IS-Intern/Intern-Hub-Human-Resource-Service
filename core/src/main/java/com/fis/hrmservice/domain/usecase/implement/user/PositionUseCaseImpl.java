package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.port.output.user.PositionRepositoryPort;

import java.util.List;

import com.fis.hrmservice.domain.usecase.command.user.AddPositionUseCaseCommand;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PositionUseCaseImpl {
    PositionRepositoryPort positionRepositoryPort;

    public List<PositionModel> listAllPosition() {
        return positionRepositoryPort.findAll();
    }

    public List<Long> findExistingPositionIds(List<Long> positionIds) {
        if (positionIds == null || positionIds.isEmpty()) {
            return List.of();
        }
        return positionRepositoryPort.findExistingPositionIds(positionIds);
    }

    public PositionModel addPosition(AddPositionUseCaseCommand command) {
        return positionRepositoryPort.addPosition(PositionModel.builder()
                .name(command.getName())
                .description(command.getDescription())
                .build());
    }
}
