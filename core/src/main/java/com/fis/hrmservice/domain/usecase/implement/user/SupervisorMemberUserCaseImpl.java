package com.fis.hrmservice.domain.usecase.implement.user;


import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupervisorMemberUserCaseImpl {

    UserRepositoryPort userRepositoryPort;

    public List<UserModel> listAllSupervisorMember(Long userId) {
        return userRepositoryPort.listMemberListBySupervisorId(userId);
    }
}
