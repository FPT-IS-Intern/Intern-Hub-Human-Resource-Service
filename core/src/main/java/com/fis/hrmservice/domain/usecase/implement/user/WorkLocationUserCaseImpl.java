package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.user.WorkLocationModel;
import com.fis.hrmservice.domain.port.output.ticket.remoteticket.WorkLocationRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.ticket.CreateWorkLocationCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.UpdateWorkLocationCommand;
import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkLocationUserCaseImpl {

    WorkLocationRepositoryPort workLocationRepositoryPort;

    public List<String> getAllWorkLocation() {
        return workLocationRepositoryPort.findAllLocationNames();
    }

    public List<WorkLocationModel> getAll() {
        return workLocationRepositoryPort.findAll();
    }

    public WorkLocationModel getById(Long id) {
        WorkLocationModel model = workLocationRepositoryPort.findById(id);
        if (model == null) {
            throw new NotFoundException("WorkLocation not found with id: " + id);
        }
        return model;
    }

    public WorkLocationModel create(CreateWorkLocationCommand command) {
        if (command.getName() == null || command.getName().trim().isEmpty()) {
            throw new BadRequestException("Name is required");
        }

        if (workLocationRepositoryPort.existByLocationName(command.getName().trim())) {
            throw new ConflictDataException("WorkLocation with name '" + command.getName() + "' already exists");
        }

        return workLocationRepositoryPort.create(WorkLocationModel.builder()
                .name(command.getName().trim())
                .address(command.getAddress())
                .description(command.getDescription())
                .build());
    }

    public WorkLocationModel update(UpdateWorkLocationCommand command) {
        if (command.getWorkLocationId() == null) {
            throw new BadRequestException("WorkLocation id is required");
        }

        WorkLocationModel existing = workLocationRepositoryPort.findById(command.getWorkLocationId());
        if (existing == null) {
            throw new NotFoundException("WorkLocation not found with id: " + command.getWorkLocationId());
        }

        if (command.getName() == null || command.getName().trim().isEmpty()) {
            throw new BadRequestException("Name is required");
        }

        String newName = command.getName().trim();
        if (!newName.equalsIgnoreCase(existing.getName())
                && workLocationRepositoryPort.existByLocationName(newName)) {
            throw new ConflictDataException("WorkLocation with name '" + newName + "' already exists");
        }

        return workLocationRepositoryPort.update(WorkLocationModel.builder()
                .workLocationId(command.getWorkLocationId())
                .name(newName)
                .address(command.getAddress())
                .description(command.getDescription())
                .build());
    }

    public String disable(Long id) {
        WorkLocationModel existing = workLocationRepositoryPort.findById(id);
        if (existing == null) {
            throw new NotFoundException("WorkLocation not found with id: " + id);
        }

        if (!Boolean.TRUE.equals(existing.getIsActive())) {
            throw new ConflictDataException("WorkLocation is already disabled");
        }

        workLocationRepositoryPort.disableWorkLocation(id);
        return "WorkLocation disabled successfully";
    }

    public String enable(Long id) {
        WorkLocationModel existing = workLocationRepositoryPort.findById(id);
        if (existing == null) {
            throw new NotFoundException("WorkLocation not found with id: " + id);
        }

        if (Boolean.TRUE.equals(existing.getIsActive())) {
            throw new ConflictDataException("WorkLocation is already enabled");
        }

        workLocationRepositoryPort.enableWorkLocation(id);
        return "WorkLocation enabled successfully";
    }
}
