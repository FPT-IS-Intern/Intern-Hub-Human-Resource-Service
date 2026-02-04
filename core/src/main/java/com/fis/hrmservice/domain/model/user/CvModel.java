package com.fis.hrmservice.domain.model.user;

import com.fis.hrmservice.domain.model.common.BaseDomain;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CvModel extends BaseDomain {
  long cvId;
  UserModel user;
  String cvUrl;
  String fileType;
  long fileSize;
  String fileName;
  String status;
}
