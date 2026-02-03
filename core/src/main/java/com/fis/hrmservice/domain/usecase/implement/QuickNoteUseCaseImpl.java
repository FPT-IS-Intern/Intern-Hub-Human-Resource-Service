package com.fis.hrmservice.domain.usecase.implement;

import com.fis.hrmservice.domain.model.user.QuickNoteModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.input.quicknote.QuickNoteUserUseCase;
import com.fis.hrmservice.domain.port.output.QuickNoteRepositoryPort;
import com.fis.hrmservice.domain.port.output.UserRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.QuickNoteCommand;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.starter.security.context.AuthContext;
import com.intern.hub.starter.security.context.AuthContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class QuickNoteUseCaseImpl implements QuickNoteUserUseCase {

    private final QuickNoteRepositoryPort quickNoteRepositoryPort;
    private final Snowflake snowflake;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public QuickNoteModel createQuickNote(QuickNoteCommand command, Long userId) {

        //TODO: đợi ong api gateway xong thì mở lại
//        AuthContext authContext = AuthContextHolder.get()
//                .orElseThrow(() -> new NotFoundException("Not authenticated"));
//
//        Long writerId = authContext.userId();
//
//        UserModel writer = userRepositoryPort.findById(writerId).orElseThrow();

        UserModel userNoted = userRepositoryPort.findById(userId).orElseThrow();

        if (userNoted == null) {
            throw new NotFoundException("User not found");
        }

        QuickNoteModel quickNoteModel = QuickNoteModel.builder()
                .id(snowflake.next())
                .intern(userNoted)
//                .writer(writer)   TODO: đợi api gateway xong thì mở lại
                .writer(null)   //TODO: đợi api gateway xong thì xoá đi
                .content(command.getContent())
                .writeDate(LocalDateTime.now())
                .build();

        return quickNoteRepositoryPort.save(quickNoteModel);
    }
}
