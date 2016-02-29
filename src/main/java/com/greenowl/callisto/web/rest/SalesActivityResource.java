package com.greenowl.callisto.web.rest;

import com.greenowl.callisto.domain.SalesRecord;
import com.greenowl.callisto.repository.SalesRecordRepository;
import com.greenowl.callisto.service.SalesRecordService;
import com.greenowl.callisto.util.SalesActivityUtil;
import com.greenowl.callisto.web.rest.dto.SalesRecordDTO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/{apiVersion}/sales")
public class SalesActivityResource {

    private static final Logger LOG = LoggerFactory.getLogger(SalesActivityResource.class);

    @Inject
    private SalesRecordService salesRecordService;

    @Inject
    private SalesRecordRepository salesRecordRepository;

    /**
     * GET /api/{version}/parking/records -> Returns a list of records between a
     * start and end date of type :type.
     */
    @RequestMapping(value = "/records", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    public ResponseEntity<?> getRecords(@PathVariable("apiVersion") final String apiVersion,
                                        @RequestParam(defaultValue = "all") final String type, @RequestParam(required = false) final Long start,
                                        @RequestParam(required = false) final Long end) {
        return getRecords(type, start, end);
    }

    /**
     * Get sales records in new table.
     *
     * @param type
     * @param start
     * @param end
     * @return
     */
    private ResponseEntity<?> getRecords(String type, Long start, Long end) {
        LOG.debug("Checking for records using type = {}, for start date = {} and end date = {}", type, start, end);
        List<SalesRecord> salesRecords;
        if (type.equals("all")) {
            salesRecords = salesRecordRepository.findAll();
        } else {
            DateTime startDate = new DateTime(start);
            DateTime endDate = new DateTime(end);
            salesRecords = salesRecordService.findAllFilteredSalesRecordsBetweenStartAndEndDate(startDate, endDate,
                    type);
        }

        List<SalesRecordDTO> salesRecordDTOs = salesRecords.stream()
                .map(salesRecord -> SalesActivityUtil.constructDTO(salesRecord, salesRecord.getActivityHolder()))
                .collect(Collectors.toList());

        LOG.info("Returning {} records", salesRecordDTOs.size());
        return new ResponseEntity<>(salesRecordDTOs, OK);

    }

    //TODO: Lingfei needs to port this to SalesRecord
//    @RequestMapping(value = "/nextDayTrans", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    @Transactional(readOnly = false)
//    public ResponseEntity<?> generateRecords(@PathVariable("apiVersion") final String apiVersion,
//                                             @RequestParam(required = false) final Long date) {
//        LOG.debug("Generate Pre transaction for  day = {}", date);
//
//        DateTime generateDate;
//        if (date == null) {
//            generateDate = DateTime.now();
//        } else {
//            generateDate = new DateTime(date);
//        }
//        DateTime startDate = generateDate.plusDays(1).withTimeAtStartOfDay();
//        DateTime endDate = generateDate.plusDays(2).withTimeAtStartOfDay();
//        List<PlanSubscription> nextDaySubscription = subscriptionService.getNextDayRenewSubscription(startDate,
//                endDate);
//        List<SalesActivityDTO> preTrans = salesActivityService.createPreTransaction(nextDaySubscription);
//        return new ResponseEntity<>(preTrans, OK);
//    }
}
