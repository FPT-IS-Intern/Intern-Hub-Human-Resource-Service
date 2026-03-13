package com.fis.hrmservice.domain.usecase.implement.user;


import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupervisorMemberUserCaseImpl {

    UserRepositoryPort userRepositoryPort;

    public List<UserModel> listAllSupervisorMember(Long userId) {
        Optional<UserModel> model = userRepositoryPort.findById(userId);
        if (model.isEmpty()) {
            throw new NotFoundException("User not found with id: " + userId);
        }

        if (!model.get().getPosition().getName().toLowerCase().contains("STAFF".toLowerCase())) {
            throw new ConflictDataException("User is not a staff member");
        }

        return userRepositoryPort.listMemberListBySupervisorId(userId);
    }
}
