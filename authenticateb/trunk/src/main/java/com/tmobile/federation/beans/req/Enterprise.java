package com.tmobile.federation.beans.req;

public class Enterprise {
	
	private String enterpriseName;
	private String corpNodeID;
	private String domain;
	private String logoUrl;
	private String[] banlist;
	private FederationInfo federationInfo;
	private Feature[] enterpriseFeatures;
	private String[] enterpriseSOC;
	private String maxUserIds;
	private AdminInfo adminInfo;
	private EnterpriseRole enterpriseRole;
	
	
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	
	public String getDomain() {
		return this.domain;
	}
	public void setDomain(String webdomain) {
		this.domain = webdomain;
	}
	
	
	public String[] getBanlist() {
		return banlist;
	}
	public void setBanlist(String[] banlist) {
		this.banlist = banlist;
	}
	
	public Feature[] getEnterpriseFeatures() {
		return enterpriseFeatures;
	}
	public void setEnterpriseFeatures(Feature[] enterpriseFeatures) {
		this.enterpriseFeatures = enterpriseFeatures;
	}
	public String[] getenterpriseSOC() {
		return enterpriseSOC;
	}
	public void setBusinessSOC(String[] enterpriseSOC) {
		this.enterpriseSOC = enterpriseSOC;
	}
	public String getMaxUserIds() {
		return maxUserIds;
	}
	public void setMaxUserIds(String maxUserIds) {
		this.maxUserIds = maxUserIds;
	}
	public EnterpriseRole getEnterpriseRole() {
		return enterpriseRole;
	}
	public void setEnterpriseRole(EnterpriseRole enterpriseRole) {
		this.enterpriseRole = enterpriseRole;
	}
	public String getCorpNodeID() {
		return corpNodeID;
	}
	public void setCorpNodeID(String corpNodeID) {
		this.corpNodeID = corpNodeID;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public FederationInfo getFederationInfo() {
		return federationInfo;
	}
	public void setFederationInfo(FederationInfo federationInfo) {
		this.federationInfo = federationInfo;
	}
	public AdminInfo getAdminInfo() {
		return adminInfo;
	}
	public void setAdminInfo(AdminInfo adminInfo) {
		this.adminInfo = adminInfo;
	}
	
	
	

}
