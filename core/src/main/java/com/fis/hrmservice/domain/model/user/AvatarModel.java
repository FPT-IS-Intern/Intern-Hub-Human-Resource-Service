package com.fis.hrmservice.domain.model.user;

import com.fis.hrmservice.domain.model.common.BaseDomain;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AvatarModel extends BaseDomain {
    Long avatarId;
    UserModel user;
    String avatarUrl;
    String fileType;
    Long fileSize;
    String fileName;
    String status;
}
