package com.fis.hrmservice.domain.service;

import com.intern.hub.library.common.utils.Snowflake;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Builder
public class GeneratedService {

    private final Snowflake snowflake;

    public Long generateId() {
        return snowflake.next();
    }

    public long getTimestamp(long id) {
        return snowflake.extractTimestamp(id);
    }

    public long getMachineId(long id) {
        return snowflake.extractMachineId(id);
    }
}
