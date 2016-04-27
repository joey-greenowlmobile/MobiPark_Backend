package com.greenowl.callisto.web.rest.parking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LogRequest {

	private List<String> logEvent;

	public List<String> getLogEvent() {
		return logEvent;
	}

	public void setLogEvent(List<String> logEvent) {
		this.logEvent = logEvent;
	}
		
}
