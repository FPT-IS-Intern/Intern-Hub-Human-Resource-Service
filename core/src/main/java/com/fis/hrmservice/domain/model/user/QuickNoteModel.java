package com.fis.hrmservice.domain.model.user;

import com.fis.hrmservice.domain.model.common.BaseDomain;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class QuickNoteModel extends BaseDomain {

    private Long id;
    private UserModel intern;
    private UserModel writer;

    private String content;
    private LocalDateTime writeDate;
    private Boolean isDeleted;
}