package com.tmobile.federation.enums;

public enum StatusEnum {
	
	PENDING("pending"),
	ACTIVE("active"),
	LOCKED("locked"),
	CANCELLED("cancelled");
	
	private String status;
	
	StatusEnum(String status){
		
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
