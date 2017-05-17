package com.tmobile.federation.beans.res;

import java.io.Serializable;

/**
 * 
 * @author Admin
 *
 */
  
public class GlobalResponseBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String statusCode;
	private Object object;
	
	public GlobalResponseBean() {
		// TODO Auto-generated constructor stub
	}
	
	public GlobalResponseBean(String statusCode, Object object){
		this.statusCode = statusCode;
		this.object = object;
	}
	
	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}
	
	
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}
	/**
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}
	
	

}
