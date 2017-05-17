package com.tmobile.federation.beans.req;


/**
 * @author Admin
 */

import java.io.Serializable;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class FedIdRequestBean implements Serializable{ 
 
	/**
	 *  
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String iamUserId;
	private String email;
	private String federatedUserId;
	private String tenantId;
	private String status;
	private String role;
	private String[] banList;
	private Object[] features;
	private String featureName;
	private String featureType;
	private Object[] featureAttributes;
	private String featureAttributeName;
	private String featureAttributeValue;
	private String[] businessSOC;
	private String[] DIDline;
	private String[] extensions;
	private String[] desklines;
	
	
	public String getIamUserId() {
		return iamUserId;
	}
	public void setIamUserId(String iamUserId) {
		this.iamUserId = iamUserId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFederatedUserId() {
		return federatedUserId;
	}
	public void setFederatedUserId(String federatedUserId) {
		this.federatedUserId = federatedUserId;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
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
	public Object[] getFeatures() {
		return features;
	}
	public void setFeatures(Object[] features) {
		this.features = features;
	}
	public String getFeatureName() {
		return featureName;
	}
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	public String getFeatureType() {
		return featureType;
	}
	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}
	public Object[] getFeatureAttributes() {
		return featureAttributes;
	}
	public void setFeatureAttributes(Object[] featureAttributes) {
		this.featureAttributes = featureAttributes;
	}
	public String getFeatureAttributeName() {
		return featureAttributeName;
	}
	public void setFeatureAttributeName(String featureAttributeName) {
		this.featureAttributeName = featureAttributeName;
	}
	public String getFeatureAttributeValue() {
		return featureAttributeValue;
	}
	public void setFeatureAttributeValue(String featureAttributeValue) {
		this.featureAttributeValue = featureAttributeValue;
	}
	public String[] getBusinessSOC() {
		return businessSOC;
	}
	public void setBusinessSOC(String[] businessSOC) {
		this.businessSOC = businessSOC;
	}
	public String[] getDIDline() {
		return DIDline;
	}
	public void setDIDline(String[] dIDline) {
		DIDline = dIDline;
	}
	public String[] getExtensions() {
		return extensions;
	}
	public void setExtensions(String[] extensions) {
		this.extensions = extensions;
	}
	public String[] getDesklines() {
		return desklines;
	}
	public void setDesklines(String[] desklines) {
		this.desklines = desklines;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tenantId == null) ? 0 : tenantId.hashCode());
		return result;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FedIdRequestBean))
			return false;
		FedIdRequestBean other = (FedIdRequestBean) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (tenantId == null) {
			if (other.tenantId != null)
				return false;
		} else if (!tenantId.equals(other.tenantId))
			return false;
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FedIdRequestBean [iamUserId=" + iamUserId + ", email=" + email + ", federatedUserId=" + federatedUserId
				+ ", tenantId=" + tenantId + ", status=" + status + ", role=" + role + ", banList="
				+ Arrays.toString(banList) + ", features=" + Arrays.toString(features) + ", featureName=" + featureName
				+ ", featureType=" + featureType + ", featureAttributes=" + Arrays.toString(featureAttributes)
				+ ", featureAttributeName=" + featureAttributeName + ", featureAttributeValue=" + featureAttributeValue
				+ ", businessSOC=" + Arrays.toString(businessSOC) + ", DIDline=" + Arrays.toString(DIDline)
				+ ", extensions=" + Arrays.toString(extensions) + ", desklines=" + Arrays.toString(desklines) + "]";
	}
 
}
