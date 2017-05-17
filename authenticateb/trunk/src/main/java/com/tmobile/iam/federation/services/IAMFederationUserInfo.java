package com.tmobile.iam.federation.services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.tmobile.federation.utils.RequestBuilder;
import com.tmobile.iam.federation.services.exceptions.IAMCreateEmptyProfileException;
import com.tmobile.iam.federation.services.res.beans.IAMCreateEmptyProfileRes;
import com.tmobile.iam.fedetaion.services.req.beans.Header;

public class IAMFederationUserInfo extends IAMBaseFederationService {

	private static final Logger logger = Logger.getLogger(IAMFederationUserInfo.class);

	public IAMCreateEmptyProfileRes createEmptyProfile(String caller) throws IAMCreateEmptyProfileException {

		logger.debug(getCreateEmptyProfileUrl() + " " + caller);
		
		ObjectMapper om = new ObjectMapper();
		IAMCreateEmptyProfileRes resObj = null;
		
		try {
			
			List<Header> headers = new ArrayList<Header>();
			headers.add(new Header("Authorization", getToken()));
			
			String emptyReq = "{}";
			logger.debug("Bio Delete Reg Req JSON is " + emptyReq);

			final BasicCookieStore cookieStore = new BasicCookieStore();
			HttpResponse<String> response = RequestBuilder.send(cookieStore,
															    "iamTimeout",
															    "POST",
															    getCreateEmptyProfileUrl(), 
															    headers, 
															    null,
															    emptyReq,
															  	"authenticateb -> iam -> createEmptyProfile -> " + caller);
			
			String output = null;
			if(response != null) {
				output = response.getBody();
			}
			
			logger.debug("Output from Server .... \n");
			logger.debug(output);
			
			if (response.getStatus() != Status.OK.getStatusCode()) {
				throw new IAMCreateEmptyProfileException("Failed : HTTP error code : "+ response.getStatus() + " " + StringEscapeUtils.escapeJson(output));
			}
			
			resObj = (IAMCreateEmptyProfileRes) om.readValue(output, IAMCreateEmptyProfileRes.class);
			logger.debug(resObj);
		} catch (Exception ex) {
			logger.error("Could not parse the output", ex);
			throw new IAMCreateEmptyProfileException("Could not parse IAM Token Response " + ex.getMessage(), ex);
		}

		return resObj;
	}	
}