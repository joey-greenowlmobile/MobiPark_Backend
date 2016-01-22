package com.greenowl.callisto.web.rest.dto;


public class ParkingPlanDTO {
	
	private Long lotId;
	
	private String planName;
	private String planDesc;
	private String unitChargeAmount;
	
	private Boolean monthlyPlan;
	private Long planTerminatedDays;
	public ParkingPlanDTO(){}
	public ParkingPlanDTO(Long lotId,String planName, String planDesc, String unitChargeAmount,Boolean monthlyPlan, Long planTerminatedDays){
		this.lotId=lotId;
		this.planName=planName;
		this.planDesc=planDesc;
		this.unitChargeAmount=unitChargeAmount;
		this.monthlyPlan=monthlyPlan;
		this.planTerminatedDays=planTerminatedDays;
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
	public String getUnitChargeAmount() {
		return unitChargeAmount;
	}
	public void setUnitChargeAmount(String unitChargeAmount) {
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
