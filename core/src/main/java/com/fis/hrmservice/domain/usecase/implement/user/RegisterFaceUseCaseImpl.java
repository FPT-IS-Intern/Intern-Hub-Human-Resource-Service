package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.feign.FaceRegistryPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.user.RegisterFaceCommand;
import com.intern.hub.library.common.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegisterFaceUseCaseImpl {

  FaceRegistryPort faceRegistryPort;
  UserRepositoryPort userRepositoryPort;

  @Transactional
  public UserModel registerFace(RegisterFaceCommand command) {

    UserModel user = userRepositoryPort
        .findById(command.getUserId())
        .orElseThrow(() -> new NotFoundException("User not found with id: " + command.getUserId()));

    boolean allRegistered = faceRegistryPort.registerFaces(command);

    if (allRegistered) {
      userRepositoryPort.updateIsFaceRegistry(command.getUserId(), true);
      user.setIsFaceRegistry(true);
      log.info("Face registered successfully for userId={}", command.getUserId());
    } else {
      log.warn("Some faces failed to register for userId={}", command.getUserId());
    }

    return user;
  }
}

