package com.greenowl.callisto.repository;

import com.greenowl.callisto.domain.PlanEligibleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlanEligibleUserRepository extends JpaRepository<PlanEligibleUser, Long> {


    @Query("select u from PlanEligibleUser u where u.userEmail = ?1")
    List<PlanEligibleUser> getEligibleUsersByUserEmail(String userEmail);

    Optional<PlanEligibleUser> findOneByUserEmail(String userEmail);

    @Query("select u from PlanEligibleUser u where u.subscribed = true")
    List<PlanEligibleUser> getSubscribedUsersByPlanId();


}
