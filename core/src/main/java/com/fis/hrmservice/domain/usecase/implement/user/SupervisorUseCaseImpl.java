package com.fis.hrmservice.domain.usecase.implement.user;

import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.intern.hub.library.common.exception.NotFoundException;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupervisorUseCaseImpl {

  UserRepositoryPort userRepositoryPort;

  public List<UserModel> listAllSupervisor() {
    return userRepositoryPort.listAllSupervisor();
  }

  @Transactional
  public void assignMentor(Long userId, Long mentorId) {
    int updatedRows = userRepositoryPort.assignMentor(userId, mentorId);
    if (updatedRows == 0) {
      throw new NotFoundException("User not found with id: " + userId);
    }
  }
}
