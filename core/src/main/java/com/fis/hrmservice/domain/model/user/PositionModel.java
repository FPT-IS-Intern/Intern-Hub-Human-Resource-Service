package com.fis.hrmservice.domain.model.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PositionModel {
    Long positionId;
    String name;
    String description;
    String status;
}
