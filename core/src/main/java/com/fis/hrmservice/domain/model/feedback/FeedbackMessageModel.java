package com.fis.hrmservice.domain.model.feedback;

import com.fis.hrmservice.domain.model.common.BaseDomain;
import com.fis.hrmservice.domain.model.user.UserModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackMessageModel extends BaseDomain {

  private long messageId;
  private FeedbackModel feedback;
  private FeedbackMessageModel parentMessage;

  private UserModel sender;
  private String senderRole;
  private String message;
}
