package com.fis.hrmservice.domain.model.user;

import com.fis.hrmservice.domain.model.common.BaseDomain;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CvModel extends BaseDomain {
    Long cvId;
    UserModel user;
    String cvUrl;
    String fileType;
    Long fileSize;
    String fileName;
    String status;
}
