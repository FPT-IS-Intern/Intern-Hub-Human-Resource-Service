package com.fis.hrmservice.domain.usecase.command.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWorkLocationCommand {
    Long workLocationId;
    String name;
    String address;
    String description;
}
