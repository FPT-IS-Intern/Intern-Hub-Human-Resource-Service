package com.fis.hrmservice.api.controller.location;

import com.fis.hrmservice.api.dto.request.WorkLocationRequest;
import com.fis.hrmservice.api.dto.response.WorkLocationResponse;
import com.fis.hrmservice.api.mapper.WorkLocationApiMapper;
import com.fis.hrmservice.domain.model.user.WorkLocationModel;
import com.fis.hrmservice.domain.usecase.command.ticket.CreateWorkLocationCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.UpdateWorkLocationCommand;
import com.fis.hrmservice.domain.usecase.implement.user.WorkLocationUserCaseImpl;
import com.intern.hub.library.common.dto.ResponseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hrm/work-locations")
@RequiredArgsConstructor
@Tag(name = "Location Management", description = "APIs for location management")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocationController {

    WorkLocationUserCaseImpl workLocationUserCase;

    WorkLocationApiMapper workLocationMapper;

    @GetMapping("/all-location")
    @Operation(summary = "Get all active location names")
    public ResponseApi<List<String>> getAllLocation() {
        return ResponseApi.ok(workLocationUserCase.getAllWorkLocation());
    }

    @GetMapping
    @Operation(summary = "Get all locations (active and inactive)")
    public ResponseApi<List<WorkLocationResponse>> getAll() {
        return ResponseApi.ok(
                workLocationUserCase.getAll().stream()
                        .map(workLocationMapper::toWorkLocationResponse)
                        .toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get location by id")
    public ResponseApi<WorkLocationResponse> getById(@PathVariable Long id) {
        WorkLocationModel model = workLocationUserCase.getById(id);
        return ResponseApi.ok(workLocationMapper.toWorkLocationResponse(model));
    }

    @PostMapping
    @Operation(summary = "Create a new work location")
    public ResponseApi<WorkLocationResponse> create(@RequestBody WorkLocationRequest request) {
        CreateWorkLocationCommand command = workLocationMapper.toCreateWorkLocationCommand(request);
        WorkLocationModel created = workLocationUserCase.create(command);
        return ResponseApi.ok(workLocationMapper.toWorkLocationResponse(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing work location")
    public ResponseApi<WorkLocationResponse> update(
            @PathVariable Long id,
            @RequestBody WorkLocationRequest request) {
        UpdateWorkLocationCommand command = workLocationMapper.toUpdateWorkLocationCommand(request);
        command.setWorkLocationId(id);
        WorkLocationModel updated = workLocationUserCase.update(command);
        return ResponseApi.ok(workLocationMapper.toWorkLocationResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Disable a work location")
    public ResponseApi<String> disable(@PathVariable Long id) {
        String result = workLocationUserCase.disable(id);
        return ResponseApi.ok(result);
    }

    @PatchMapping("/{id}/enable")
    @Operation(summary = "Enable a work location")
    public ResponseApi<String> enable(@PathVariable Long id) {
        String result = workLocationUserCase.enable(id);
        return ResponseApi.ok(result);
    }
}