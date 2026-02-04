package com.fis.hrmservice.domain.model.ticket;

import com.fis.hrmservice.domain.model.common.BaseDomain;
import com.fis.hrmservice.domain.model.constant.TicketStatus;
import com.fis.hrmservice.domain.model.user.UserModel;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketModel extends BaseDomain {

  Long ticketId;
  UserModel requester;
  TicketTypeModel ticketType;
  LocalDate startAt;
  LocalDate endAt;
  String reason;
  TicketStatus sysStatus;
}
