package com.tmobile.wsg.services;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.tmobile.federation.dao.IAMUserDAOImpl;
import com.tmobile.federation.utils.FederationAppConfig;
import com.tmobile.mauth.util.AppConfig;

public abstract class WSGBaseFederationService {

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(WSGBaseFederationService.class);
	private String createEnterpriseUrl;
	private String wsgCDBUserProfileUrl;


	public WSGBaseFederationService() {
		
		String wsgHostPort = AppConfig.getProperty("wsgHostPort");

		String createEnterpriseUrl = FederationAppConfig.getProperty("wsgCreateEnterpriseHostUrl");
		
		Object[] objs = {wsgHostPort};
		MessageFormat formatUrl = new MessageFormat(createEnterpriseUrl);
		setCreateEnterpriseUrl(formatUrl.format(objs));

		//for retrying CDB Profile
		String getCDBUserProfileUrl = FederationAppConfig.getProperty("wsgCDBUserProfileUrl");
		MessageFormat formatCDBUrl = new MessageFormat(getCDBUserProfileUrl);
		setCDBUserProfileUrl(formatCDBUrl.format(objs));

		logger.info("createEnterpriseUrl:"+createEnterpriseUrl+" wsgCDBUserProfileUrl:"+wsgCDBUserProfileUrl);
	}
	
	public String getCDBUserProfileUrl() {
		return wsgCDBUserProfileUrl;
	}

	public void setCDBUserProfileUrl(String getCDBUserProfileUrl) {
		this.wsgCDBUserProfileUrl = getCDBUserProfileUrl;
	}


	public String getCreateEnterpriseUrl() {
		return createEnterpriseUrl;
	}

	public void setCreateEnterpriseUrl(String createEnterpriseUrl) {
		this.createEnterpriseUrl = createEnterpriseUrl;
	}


}
