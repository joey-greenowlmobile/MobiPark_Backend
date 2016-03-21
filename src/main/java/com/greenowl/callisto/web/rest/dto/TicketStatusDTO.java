package com.greenowl.callisto.web.rest.dto;

public class TicketStatusDTO {

	private Long ticketNo;
	
	private String accessTime;
	
	private String ticketStatus;
	
	private String opStatus;
	
	public TicketStatusDTO(Long ticketNo, String accessTime, String ticketStatus, String opStatus){
		this.ticketNo = ticketNo;
		this.accessTime = accessTime;
		this.ticketStatus = ticketStatus;
		this.opStatus = opStatus;
	}

	public Long getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(Long ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(String accessTime) {
		this.accessTime = accessTime;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public String getOpStatus() {
		return opStatus;
	}

	public void setOpStatus(String opStatus) {
		this.opStatus = opStatus;
	}
	
	
	
}
