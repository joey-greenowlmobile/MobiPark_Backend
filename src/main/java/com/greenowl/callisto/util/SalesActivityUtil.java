package com.greenowl.callisto.util;

import com.greenowl.callisto.domain.SalesRecord;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.web.rest.dto.SalesRecordDTO;

/**
 * Created by ahmed on 16-02-29.
 */
public class SalesActivityUtil {

    public static SalesRecordDTO constructDTO(SalesRecord activity, User user) {
        return new SalesRecordDTO(activity, user);
    }

}
