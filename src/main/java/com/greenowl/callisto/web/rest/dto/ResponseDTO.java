package com.greenowl.callisto.web.rest.dto;

public class ResponseDTO {
	private String result;
	public ResponseDTO(){};
	public ResponseDTO(String result){
		this.result=result;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	@Override
    public String toString() {
        return "ResponseDTO{" +
                "result='" + result+ 
                '}';
    }
}
