package com.fis.hrmservice.domain.model.feedback;

import com.fis.hrmservice.domain.model.common.BaseDomain;
import com.fis.hrmservice.domain.model.user.UserModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnonymousFeedBackModel extends BaseDomain {
  long feedbackId;
  FeedbackTypeModel feedbackType;
  String title;
  String content;
  UserModel sender;
}
