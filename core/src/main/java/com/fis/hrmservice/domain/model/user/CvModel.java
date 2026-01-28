package com.fis.hrmservice.domain.model.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CvModel {
    Long cvId;
    Long userId;
    String cvUrl;
}
