package com.tmobile.federation.utils;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;


public class Constants {

	public static final String IAM_SESSION_ID = "iam_session_id";
	public static final String IAM_USER_ID = "iam_user_id";
	public static final String REQ_HDR_AUTHORIZATION = "Authorization";
	
	public static final String HOSTNAME_RES_HEADER_NAME = "X-S-N";
	
	public static final String TMOID_KEY = "TMOID";
	public static final String EMAIL_KEY = "EMAIL";

	public static final String MSISDN_KEY = "MSISDN";

	public static final String IMSI_KEY = "IMSI";
	public static final String DEVICE_ID_KEY = "DEVICE_ID";
	
	public static final String MOBILE= "mobile";
	public static final String DESKTOP= "desktop";
	
	//public static final String DEFAULT_RESPONSE_TYPE = ResponseTypeEnum.CODE.toString().toLowerCase();
	
	public static final String FORCE_REAUTH = "force";
	
	public static final String ACTIVE_MSISDN_ACCT_STATUS = "A";
	
	public static final List<MediaType> ALLOWED_REQ_HDR_TYPES = new ArrayList<MediaType>();
	public static final List<MediaType> ALLOWED_REQ_ACCEPT_HDR_TYPES = new ArrayList<MediaType>();
	
	static {
		ALLOWED_REQ_HDR_TYPES.add(MediaType.APPLICATION_JSON_TYPE);
		ALLOWED_REQ_ACCEPT_HDR_TYPES.add(MediaType.APPLICATION_JSON_TYPE);
	}
	
}
