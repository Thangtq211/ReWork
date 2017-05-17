package com.tmobile.federation.beans.res;

public class EnterpriseResponse {
  
  private int statusCode;
  private String shortDesc;
  private String longDesc;
  private int httpResponseCode;
  
    public EnterpriseResponse() {
    	
    }
    
    public EnterpriseResponse(int statusCode, String shortDesc) {
    	this.statusCode = statusCode;
    	this.shortDesc = shortDesc;
    }
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getLongDesc() {
		return longDesc;
	}
	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	@Override
	public String toString() {
		return "EnterpriseResponse [statusCode=" + statusCode + ", longDesc=" + longDesc + ", shortDesc=" + shortDesc
				+ "]";
	}

	public int getHttpResponseCode() {
		return httpResponseCode;
	}

	public void setHttpResponseCode(int httpResponseCode) {
		this.httpResponseCode = httpResponseCode;
	}
 
  
  
}
