package com.fis.hrmservice.domain.usecase.implement.quicknote;

import com.fis.hrmservice.domain.model.user.QuickNoteModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.quicknote.QuickNoteRepositoryPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.quicknote.QuickNoteCommand;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.library.common.utils.Snowflake;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuickNoteUseCaseImpl {

  @Autowired
  private QuickNoteRepositoryPort quickNoteRepositoryPort;
  @Autowired
  private Snowflake snowflake;
  @Autowired
  private UserRepositoryPort userRepositoryPort;

  public QuickNoteModel createQuickNote(QuickNoteCommand command, Long userId) {

    // TODO: đợi ong api gateway xong thì mở lại
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

    QuickNoteModel quickNoteModel =
        QuickNoteModel.builder()
            .id(snowflake.next())
            .intern(userNoted)
            //                .writer(writer)   TODO: đợi api gateway xong thì mở lại
            .writer(null) // TODO: đợi api gateway xong thì xoá đi
            .content(command.getContent())
            .writeDate(LocalDateTime.now())
            .build();

    return quickNoteRepositoryPort.save(quickNoteModel);
  }
}
