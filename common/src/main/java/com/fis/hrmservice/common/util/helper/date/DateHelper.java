package com.fis.hrmservice.common.util.helper.date;

import com.intern.hub.library.common.utils.DateTimeHelper;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DateHelper {

    public Instant toInstant(long milliSeconds) {
        return DateTimeHelper.toInstant(milliSeconds);
    }

}
