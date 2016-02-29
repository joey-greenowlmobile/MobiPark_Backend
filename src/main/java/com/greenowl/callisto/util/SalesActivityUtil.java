package com.greenowl.callisto.util;

import com.greenowl.callisto.domain.ParkingSaleActivity;
import com.greenowl.callisto.domain.SalesRecord;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.web.rest.dto.SalesActivityDTO;
import com.greenowl.callisto.web.rest.dto.SalesRecordDTO;

/**
 * Created by ahmed on 16-02-29.
 */
public class SalesActivityUtil {

    public static SalesRecordDTO constructDTO(SalesRecord activity, User user) {
        return new SalesRecordDTO(activity, user);
    }

    public static SalesActivityDTO constructDTO(ParkingSaleActivity activity, User user) {
        SalesActivityDTO salesActivityDTO = new SalesActivityDTO(activity.getId(), activity.getLotId(), user,
                activity.getPlanId(), activity.getPlanName(), activity.getPlanSubscriptionDate(),
                activity.getPlanExpiryDate(), activity.getChargeAmount(), activity.getServiceAmount(),
                activity.getNetAmount(), activity.getPpId(), activity.getEntryDatetime(), activity.getEntryDatetime(),
                activity.getParkingStatus(), activity.getExceptionFlag(), activity.getInvoiceId());
        salesActivityDTO.setGateResponse(activity.getGateResponse());
        return salesActivityDTO;
    }
}
