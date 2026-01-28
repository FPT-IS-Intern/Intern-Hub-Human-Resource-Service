package com.fis.hrmservice.domain.model.feedback;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackType {
    Long feedbackTypeId;
    String code;
    String name;
    String description;
}
