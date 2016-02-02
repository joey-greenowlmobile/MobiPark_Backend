package com.greenowl.callisto.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "T_PARKING_PLAN_ELIGIBLE_USER")
public class PlanEligibleUser extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Access(AccessType.PROPERTY)
    private Long id;


    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "subscribed", nullable = false)
    private Boolean subscribed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    private ParkingPlan planGroup;

    public PlanEligibleUser() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }

    public ParkingPlan getPlanGroup() {
        return planGroup;
    }

    public void setPlanGroup(ParkingPlan planGroup) {
        this.planGroup = planGroup;
    }


}
