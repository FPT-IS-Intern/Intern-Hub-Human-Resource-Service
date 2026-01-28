package com.fis.hrmservice.domain.model.ticket;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Evidence {
    Long evidenceId;
    Long ticketId;
    String evidenceFolder;
    String evidenceUrl;
    LocalDateTime uploadAt;
}