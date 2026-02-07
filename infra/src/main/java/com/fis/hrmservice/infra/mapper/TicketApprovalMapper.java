package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.ticket.TicketApprovalModel;
import com.fis.hrmservice.infra.persistence.entity.TicketApproval;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TicketMapper.class, UserMapper.class})
public interface TicketApprovalMapper {

    @Mapping(source = "id", target = "approvalId")
    @Mapping(source = "actionAt", target = "actionAt", qualifiedByName = "localDateToEpoch")
    TicketApprovalModel toModel(TicketApproval entity);

    @Mapping(source = "approvalId", target = "id")
    @Mapping(source = "actionAt", target = "actionAt", qualifiedByName = "epochMillisToLocalDate")
    TicketApproval toEntity(TicketApprovalModel model);
}
