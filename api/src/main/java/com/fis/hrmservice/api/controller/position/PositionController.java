package com.fis.hrmservice.api.controller.position;

import com.fis.hrmservice.api.dto.request.AddPositionRequest;
import com.fis.hrmservice.api.dto.response.PositionListResponse;
import com.fis.hrmservice.api.mapper.PositionApiMapper;
import com.fis.hrmservice.domain.usecase.implement.user.PositionUseCaseImpl;
import com.intern.hub.library.common.dto.ResponseApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("hrm/users/positions")
@Tag(name = "Position Management", description = "APIs for position management")
//@CrossOrigin(origins = "http://localhost:4205")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PositionController {

  PositionUseCaseImpl positionUseCase;

  PositionApiMapper positionMapper;

  @GetMapping
  public ResponseApi<List<PositionListResponse>> listAllPosition() {
    return ResponseApi.ok(
        positionUseCase.listAllPosition().stream()
            .map(positionMapper::toPositionListResponse)
            .toList());
  }

  @PostMapping("/creating-position")
  public ResponseApi<?> createPosition(@RequestBody AddPositionRequest request) {
    return ResponseApi.ok(positionUseCase.addPosition(positionMapper.toAddPositionCommand(request)));
  }
}
