package com.fis.hrmservice.domain.usecase.implement.quicknote;

import com.fis.hrmservice.domain.model.user.QuickNoteModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.quicknote.QuickNoteRepositoryPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.quicknote.QuickNoteCommand;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.library.common.utils.Snowflake;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuickNoteUseCaseImpl {

  private final QuickNoteRepositoryPort quickNoteRepositoryPort;
  private final Snowflake snowflake;
  private final UserRepositoryPort userRepositoryPort;

  public QuickNoteModel createQuickNote(QuickNoteCommand command, Long userId, Long writerId) {

    // TODO: đợi ong api gateway xong thì mở lại
    //        AuthContext authContext = AuthContextHolder.get()
    //                .orElseThrow(() -> new NotFoundException("Not authenticated"));
    //
    //        Long writerId = authContext.userId();
    //
    UserModel writer =
        userRepositoryPort
            .findById(writerId)
            .orElseThrow(() -> new NotFoundException("Writer not found"));

    UserModel userNoted =
        userRepositoryPort
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));

    QuickNoteModel quickNoteModel =
        QuickNoteModel.builder()
            .id(snowflake.next())
            .intern(userNoted)
            .writer(writer)
            .content(command.getContent())
            .writeDate(LocalDateTime.now())
            .build();

    return quickNoteRepositoryPort.save(quickNoteModel);
  }

  public List<QuickNoteModel> findAllByUserId(Long userId) {
    return quickNoteRepositoryPort.findAllByUserId(userId);
  }
}
