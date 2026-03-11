package com.fis.hrmservice.domain.model.resonse;

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
