package com.fis.hrmservice.domain.port.output.feign;

import com.fis.hrmservice.domain.usecase.command.user.RegisterFaceCommand;

public interface FaceRegistryPort {

  /**
   * Call the external face recognition service to register faces.
   *
   * @param command contains userName and 9 face images
   * @return true if all faces were registered successfully
   */
  boolean registerFaces(RegisterFaceCommand command);
}

