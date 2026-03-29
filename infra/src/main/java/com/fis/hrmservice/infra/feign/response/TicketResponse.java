package com.fis.hrmservice.infra.feign.response;


import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

public record TicketResponse(
        @JsonSerialize(using = ToStringSerializer.class) Long ticketId,

        String status) {
}
