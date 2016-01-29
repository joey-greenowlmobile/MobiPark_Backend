package com.greenowl.callisto.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "T_PARKING_PLAN")
public class ParkingPlan  extends AbstractAuditingEntity implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Access(AccessType.PROPERTY)
    private Long id;
	
	@Column(name = "lot_id", nullable = false)
	private Long lotId;
	
	@Column(name = "plan_name", nullable = false)
	private String planName;
	
	@Column(name = "plan_desc", nullable = false)
	private String planDesc;
	
	@Column(name = "unit_charge_amount", nullable = false)
	private Double unitChargeAmount;
	
	@Column(name = "monthly_plan", nullable = false)
	private Boolean monthlyPlan;
	
	@Column(name = "plan_terminated_days", nullable = false)
	private Long planTerminatedDays;
	
	private Boolean active;

	

    @JsonIgnore
    @OneToMany(mappedBy = "planGroup", targetEntity = PlanEligibleUser.class, fetch = FetchType.LAZY)
    private Set<PlanEligibleUser> planEligibleUsers = new HashSet<>();


    @JsonIgnore
    @OneToMany(mappedBy = "planGroup", targetEntity = PlanSubscription.class, fetch = FetchType.LAZY)
    private Set<PlanSubscription> planSubscriptions = new HashSet<>();
    
	public ParkingPlan(){}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLotId() {
		return lotId;
	}

	public void setLotId(Long lotId) {
		this.lotId = lotId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getPlanDesc() {
		return planDesc;
	}

	public void setPlanDesc(String planDesc) {
		this.planDesc = planDesc;
	}

	public Double getUnitChargeAmount() {
		return unitChargeAmount;
	}

	public void setUnitChargeAmount(Double unitChargeAmount) {
		this.unitChargeAmount = unitChargeAmount;
	}

	public Boolean getMonthlyPlan() {
		return monthlyPlan;
	}

	public void setMonthlyPlan(Boolean monthlyPlan) {
		this.monthlyPlan = monthlyPlan;
	}

	public Long getPlanTerminatedDays() {
		return planTerminatedDays;
	}

	public void setPlanTerminatedDays(Long planTerminatedDays) {
		this.planTerminatedDays = planTerminatedDays;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Set<PlanEligibleUser> getPlanEligibleUsers() {
		return planEligibleUsers;
	}

	public void setPlanEligibleUsers(Set<PlanEligibleUser> planEligibleUsers) {
		this.planEligibleUsers = planEligibleUsers;
	}

	public Set<PlanSubscription> getPlanSubscriptions() {
		return planSubscriptions;
	}

	public void setPlanSubscriptions(Set<PlanSubscription> planSubscriptions) {
		this.planSubscriptions = planSubscriptions;
	}
	
	
	
}
