package com.greenowl.callisto.service;

import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.PlanEligibleUser;
import com.greenowl.callisto.repository.ParkingPlanRepository;
import com.greenowl.callisto.repository.PlanEligibleUserRepository;
import com.greenowl.callisto.web.rest.dto.ParkingPlanDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class ParkingPlanService {
    private final Logger LOG = LoggerFactory.getLogger(ParkingPlanService.class);

    @Inject
    private ParkingPlanRepository parkingPlanRepository;

    @Inject
    private PlanEligibleUserRepository planEligibleUserRepository;

    public Set<PlanEligibleUser> getEligibleUsersByPlanId(Long id) {
        ParkingPlan parkingPlan = getParkingPlanById(id);
        if (parkingPlan != null) {
            return parkingPlan.getPlanEligibleUsers();
        }

        return null;
    }

    public ParkingPlan getParkingPlanById(Long id) {
        return parkingPlanRepository.getOneParkingPlanById(id);
    }

    public List<ParkingPlan> getAllRucurringPlan() {
        return parkingPlanRepository.getRecurringParkingPlans();
    }

    public ParkingPlan etParkingPlanByName(String planName) {
        return parkingPlanRepository.getOneParkingPlanByPlanName(planName);
    }

    public ParkingPlanService() {
    }

    public ParkingPlanDTO createParkingPlanInformation(ParkingPlan parkingPlan) {
        ParkingPlanDTO parkingPlanDTO = new ParkingPlanDTO(parkingPlan.getId(),
                parkingPlan.getPlanName(),
                parkingPlan.getPlanDesc(),
                parkingPlan.getUnitChargeAmount(),
                parkingPlan.getMonthlyPlan(),
                parkingPlan.getPlanTerminatedDays());
        return parkingPlanDTO;
    }

}
