package com.tmobile.federation.beans.req;

public class EnterpriseRole {
	private String role;
	private String[] banList;
	private Boolean isRoleAdmin;
	private Boolean isUserAdmin;
	private Boolean isGroupAdmin;
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String[] getBanList() {
		return banList;
	}
	public void setBanList(String[] banList) {
		this.banList = banList;
	}
	public Boolean getIsRoleAdmin() {
		return isRoleAdmin;
	}
	public void setIsRoleAdmin(Boolean isRoleAdmin) {
		this.isRoleAdmin = isRoleAdmin;
	}
	public Boolean getIsUserAdmin() {
		return isUserAdmin;
	}
	public void setIsUserAdmin(Boolean isUserAdmin) {
		this.isUserAdmin = isUserAdmin;
	}
	public Boolean getIsGroupAdmin() {
		return isGroupAdmin;
	}
	public void setIsGroupAdmin(Boolean isGroupAdmin) {
		this.isGroupAdmin = isGroupAdmin;
	}
	
}
