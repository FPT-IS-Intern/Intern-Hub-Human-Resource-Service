package com.fis.hrmservice.domain.model.user;

import com.fis.hrmservice.domain.model.common.BaseDomain;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AvatarModel extends BaseDomain {
  long avatarId;
  UserModel user;
  String avatarUrl;
  String fileType;
  long fileSize;
}
