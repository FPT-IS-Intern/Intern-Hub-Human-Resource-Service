package com.fis.hrmservice.domain.model.feedback;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackMessage {
    Long messageId;
    Long feedbackId;
    Long senderId;
    Long parentMessageId;
    String senderRole;
    String message;
    String content;
}
