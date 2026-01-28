package com.fis.hrmservice.domain.model.feedback;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnonymousFeedbacks {
    Long feedbackId;
    Long feedbackTypeId;
    String title;
    String content;
    Long senderId;
}