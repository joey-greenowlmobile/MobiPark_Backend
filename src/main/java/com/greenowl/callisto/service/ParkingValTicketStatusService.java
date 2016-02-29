package com.greenowl.callisto.service;

import com.greenowl.callisto.domain.ParkingValTicketStatus;
import com.greenowl.callisto.repository.ParkingValTicketStatusRepository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;


@Service
public class ParkingValTicketStatusService {

    @Inject
    private ParkingValTicketStatusRepository pvtsRepository;


    public void createParkingValTicketStatus(long ticketNo, int ticketType, DateTime time) {
        ParkingValTicketStatus p = new ParkingValTicketStatus();
        p.setTicketNo(ticketNo);
        p.setTicketType(ticketType);
        p.setCreatedDateTime(time);
        pvtsRepository.save(p);
    }

    public void createParkingValTicketStatus(ParkingValTicketStatus parkingValTicketStatus) {
        pvtsRepository.save(parkingValTicketStatus);
    }

    public void updateValidateTime(DateTime time, long id) {
        pvtsRepository.updateValidateTime(time, id);
    }

    public void updateValidatedFlag(int flag, long id) {
        pvtsRepository.updateValidatedFlag(flag, id);
    }

    public ParkingValTicketStatus getParkingValTicketStatusById(long id) {
        return pvtsRepository.getParkingValTicketStatusById(id);
    }

    public List<ParkingValTicketStatus> getParkingValTicketStatusByTicketnoAndTicketType(long ticketNo, int ticketType) {
        return pvtsRepository.getParkingValTicketStatusByTicketnoAndTickettype(ticketNo, ticketType);
    }


}
