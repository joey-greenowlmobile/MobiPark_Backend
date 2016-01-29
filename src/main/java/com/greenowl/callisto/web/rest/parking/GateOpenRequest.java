package com.greenowl.callisto.web.rest.parking;

public class GateOpenRequest {
	private Long lotId;
	private Double gateDistance;
	private Long gateId;
	public GateOpenRequest(){}
	public GateOpenRequest(Long lotId, Double gateDistance, Long gateId){
		this.lotId=lotId;
		this.gateDistance=gateDistance;
		this.gateId=gateId;
	}
	public Long getLotId() {
		return lotId;
	}
	public void setLotId(Long lotId) {
		this.lotId = lotId;
	}
	public Double getGateDistance() {
		return gateDistance;
	}
	public void setGateDistance(Double gateDistance) {
		this.gateDistance = gateDistance;
	}
	public Long getGateId() {
		return gateId;
	}
	public void setGateId(Long gateId) {
		this.gateId = gateId;
	}
	@Override
    public String toString() {
        return "GateOpenRequest{" +
                ", lotId='" + lotId + '\'' +
                ", gateDistance='" + gateDistance + '\'' +
                ", gateId='" + gateId  +
                '}';
    }
}
