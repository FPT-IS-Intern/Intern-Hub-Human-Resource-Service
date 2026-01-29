package com.fis.hrmservice.domain.usecase.command;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilterUserCommand {
    String keyword;
    String sysStatus;
    String position;
    String role;
}