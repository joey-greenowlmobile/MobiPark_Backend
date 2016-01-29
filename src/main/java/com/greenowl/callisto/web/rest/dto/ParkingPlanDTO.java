package com.greenowl.callisto.web.rest.dto;


public class ParkingPlanDTO {
	
	private Long planId;
	
	private String planName;
	private String planDesc;
	private Double unitChargeAmount;
	
	private Boolean monthlyPlan;
	private Long planTerminatedDays;
	public ParkingPlanDTO(){}
	public ParkingPlanDTO(Long planId,String planName, String planDesc, Double unitChargeAmount,Boolean monthlyPlan, Long planTerminatedDays){
		this.planId=planId;
		this.planName=planName;
		this.planDesc=planDesc;
		this.unitChargeAmount=unitChargeAmount;
		this.monthlyPlan=monthlyPlan;
		this.planTerminatedDays=planTerminatedDays;
	}
	public Long getPlanId() {
		return planId;
	}
	public void setPlanId(Long planId) {
		this.planId = planId;
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
	

    @Override
    public String toString() {
    	return "planDTO{" +
                "planName='" + planName + '\'' +
                ", planDesc='" + planDesc + '\'' +
                ", unitChargeAmount='" + unitChargeAmount + '\'' +
                ", monthlyPlan='" + monthlyPlan.toString() + '\'' +
                ", planTerminatedDays='" + planTerminatedDays + 
                '}';
    }
}
