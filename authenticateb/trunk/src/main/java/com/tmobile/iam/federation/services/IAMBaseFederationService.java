package com.tmobile.iam.federation.services;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.tmobile.federation.utils.FederationAppConfig;
import com.tmobile.mauth.util.AppConfig;

public abstract class IAMBaseFederationService {

	public String IAM_SESSION_ID = "iam_session_id";
	
	private static List<String> DEVICE_TYPE_LIST = new ArrayList<String>();
	static {
		DEVICE_TYPE_LIST.add("mobile");
		DEVICE_TYPE_LIST.add("desktop");
	}
	
	private String createEmptyProfileUrl;
	private String token;
	private String iamBUserInfoUrl;
	
	public IAMBaseFederationService() {
		
		String iamHostPort = AppConfig.getProperty("iamHostPort");

		String createEmptyProfileUrl = FederationAppConfig.getProperty("createEmptyProfileUrl");
		String iamUserInfoUrl = FederationAppConfig.getProperty("iamUserInfoUrl");
		
		Object[] objs = {iamHostPort};
		MessageFormat formatUrl = new MessageFormat(createEmptyProfileUrl);
		setCreateEmptyProfileUrl(formatUrl.format(objs));
		
		formatUrl = new MessageFormat(iamBUserInfoUrl);
		setIamBUserInfoUrl(formatUrl.format(objs));
		
		
	}

	public String getIamBUserInfoUrl() {
		return iamBUserInfoUrl;
	}

	public void setIamBUserInfoUrl(String iamBUserInfoUrl) {
		this.iamBUserInfoUrl = iamBUserInfoUrl;
	}

	public String getCreateEmptyProfileUrl() {
		return createEmptyProfileUrl;
	}

	public void setCreateEmptyProfileUrl(String createEmptyProfileUrl) {
		this.createEmptyProfileUrl = createEmptyProfileUrl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}