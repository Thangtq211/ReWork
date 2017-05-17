package com.tmobile.federation.beans.req;

public class FederationInfo {
	private Boolean federationEnabled;
	private String federationProvider;
	private Boolean autoUserOnboardingEnabled;
	private String autoUserOnboardingRole;
	
	
	public String getFederationProvider() {
		return federationProvider;
	}
	public void setFederationProvider(String federationProvider) {
		this.federationProvider = federationProvider;
	}
	
	public String getAutoUserOnboardingRole() {
		return autoUserOnboardingRole;
	}
	public void setAutoUserOnboardingRole(String autoUserOnboardingRole) {
		this.autoUserOnboardingRole = autoUserOnboardingRole;
	}
	public Boolean getFederationEnabled() {
		return federationEnabled;
	}
	public void setFederationEnabled(Boolean federationEnabled) {
		this.federationEnabled = federationEnabled;
	}
	public Boolean getAutoUserOnboardingEnabled() {
		return autoUserOnboardingEnabled;
	}
	public void setAutoUserOnboardingEnabled(Boolean autoUserOnboardingEnabled) {
		this.autoUserOnboardingEnabled = autoUserOnboardingEnabled;
	}
	
	
}
