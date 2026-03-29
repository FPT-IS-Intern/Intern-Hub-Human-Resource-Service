package com.fis.hrmservice.domain.model.dto.resonse;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleCoreResponse {
    String id;
    String name;
    String description;
    String status;
}
