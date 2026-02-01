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
public class FeedbackModel extends BaseDomain {

    private Long feedbackId;
    private FeedbackTypeModel feedbackType;
    private String title;
    private String content;
    private UserModel creator;
}