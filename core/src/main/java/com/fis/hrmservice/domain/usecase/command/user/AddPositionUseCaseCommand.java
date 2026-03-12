package com.fis.hrmservice.domain.usecase.command.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddPositionUseCaseCommand {
    String name;
    String description;
}
