package com.tmobile.federation.beans.res;

import java.io.Serializable;

/**
 * 
 * @author Admin
 *
 */

public class GlobalErrorBean {

	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;
	private String errorDescription;
	private String errorDetail;
	
	
	
	public String getErrorDetail() {
		return errorDetail;
	}
	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

}
