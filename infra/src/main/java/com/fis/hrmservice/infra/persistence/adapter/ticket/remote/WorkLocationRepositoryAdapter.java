package com.fis.hrmservice.infra.persistence.adapter.ticket.remote;

import com.fis.hrmservice.domain.model.user.WorkLocationModel;
import com.fis.hrmservice.domain.port.output.ticket.remoteticket.WorkLocationRepositoryPort;
import com.fis.hrmservice.infra.mapper.WorkLocationMapper;
import com.fis.hrmservice.infra.persistence.entity.WorkLocation;
import com.fis.hrmservice.infra.persistence.repository.ticket.WorkLocationRepository;
import com.intern.hub.library.common.utils.Snowflake;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WorkLocationRepositoryAdapter implements WorkLocationRepositoryPort {

  private final WorkLocationMapper workLocationMapper;

  private final WorkLocationRepository workLocationRepository;

  private final Snowflake snowflake;

  @Override
  public boolean existByLocationName(String locationName) {
    return workLocationRepository.existsByName(locationName);
  }

  @Override
  public List<String> findAllLocationNames() {
    return workLocationRepository.getAllWorkLocationName();
  }

  @Override
  public WorkLocationModel findByLocationName(String locationName) {
    return workLocationMapper.toModel(workLocationRepository.findByName(locationName));
  }

  @Override
  public List<WorkLocationModel> findAll() {
    return workLocationRepository.findAll().stream()
        .map(workLocationMapper::toModel)
        .toList();
  }

  @Override
  public WorkLocationModel findById(Long id) {
    return workLocationRepository.findById(id)
        .map(workLocationMapper::toModel)
        .orElse(null);
  }

  @Override
  public WorkLocationModel save(WorkLocationModel workLocation) {
    WorkLocation entity = workLocationMapper.toEntity(workLocation);
    return workLocationMapper.toModel(workLocationRepository.save(entity));
  }

  @Override
  public WorkLocationModel create(WorkLocationModel workLocation) {
    WorkLocation entity = new WorkLocation();
    entity.setId(snowflake.next());
    entity.setName(workLocation.getName());
    entity.setAddress(workLocation.getAddress());
    entity.setDescription(workLocation.getDescription());
    entity.setIsActive(true);
    return workLocationMapper.toModel(workLocationRepository.save(entity));
  }

  @Override
  public WorkLocationModel update(WorkLocationModel workLocation) {
    WorkLocation entity = workLocationRepository.findById(workLocation.getWorkLocationId())
        .orElseThrow(() -> new RuntimeException("WorkLocation not found with id: " + workLocation.getWorkLocationId()));
    entity.setName(workLocation.getName());
    entity.setAddress(workLocation.getAddress());
    entity.setDescription(workLocation.getDescription());
    return workLocationMapper.toModel(workLocationRepository.save(entity));
  }

  @Override
  public int disableWorkLocation(Long id) {
    return workLocationRepository.disableWorkLocation(id);
  }

  @Override
  public int enableWorkLocation(Long id) {
    return workLocationRepository.enableWorkLocation(id);
  }
}
