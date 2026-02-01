package com.fis.hrmservice.domain.model.feedback;

import com.fis.hrmservice.domain.model.common.BaseDomain;
import com.fis.hrmservice.domain.model.user.UserModel;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class FeedbackAttachmentModel extends BaseDomain {
    Long attachmentId;
    FeedbackMessageModel feedbackMessage;
    UserModel uploader;
    AnonymousFeedBackModel feedback;
    String fileName;
    String fileUrl;
    String fileType;
    Long fileSize;
}
