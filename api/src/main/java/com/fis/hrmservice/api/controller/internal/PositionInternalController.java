package com.fis.hrmservice.api.controller.internal;

import com.fis.hrmservice.api.dto.response.PositionListResponse;
import com.fis.hrmservice.api.mapper.PositionApiMapper;
import com.fis.hrmservice.domain.usecase.implement.user.PositionUseCaseImpl;
import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.starter.security.annotation.Internal;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hrm/internal/positions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PositionInternalController {

  PositionUseCaseImpl positionUseCase;
  PositionApiMapper positionApiMapper;

  @GetMapping
  @Internal
  public ResponseApi<List<PositionListResponse>> listAllPositionsInternal() {
    return ResponseApi.ok(
        positionUseCase.listAllPosition().stream()
            .map(positionApiMapper::toPositionListResponse)
            .toList());
  }
}

