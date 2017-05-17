package com.tmobile.federation.beans.req;

public class EnterpriseRequest {

@Override
	public String toString() {
		return "EnterpriseRequest [enterpriseProfile=" + enterpriseProfile + "]";
	}

private  Enterprise enterpriseProfile;

public Enterprise getEnterpriseProfile() {
	return enterpriseProfile;
}

public void setEnterpriseProfile(Enterprise enterpriseProfile) {
	this.enterpriseProfile = enterpriseProfile;
}
}   
