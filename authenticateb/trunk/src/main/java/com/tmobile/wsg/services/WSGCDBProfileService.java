package com.tmobile.wsg.services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.tmobile.federation.beans.req.Attribute;
import com.tmobile.federation.beans.req.Enterprise;
import com.tmobile.federation.beans.req.Feature;
import com.tmobile.federation.beans.req.FedIdRequestBean;
import com.tmobile.federation.beans.res.EnterpriseResponse;
import com.tmobile.federation.beans.req.userProfile.ListDef;
import com.tmobile.federation.beans.req.userProfile.ListItemDef;
import com.tmobile.federation.beans.req.userProfile.UserProfiles;
import com.tmobile.federation.beans.res.GlobalErrorBean;
import com.tmobile.federation.beans.res.GlobalResponseBean;
import com.tmobile.federation.utils.RequestBuilder;
import com.tmobile.iam.fedetaion.services.req.beans.Header;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response.Status;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class WSGCDBProfileService extends WSGBaseFederationService {
	private static final Logger logger = Logger.getLogger(WSGCDBProfileService.class);

	public  GlobalResponseBean getCDBUserProfile(String tuid,FedIdRequestBean response)  {
		logger.debug(getCDBUserProfileUrl() + " tuid:"+tuid + " response:"+ response);

		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			ResponseEntity<UserProfiles> responseEntity =
			restTemplate.exchange(getCDBUserProfileUrl() + "/"+tuid, HttpMethod.GET, entity, UserProfiles.class);
			logger.debug("responseEntity UserProfiles from Server .... \n");
			logger.debug(responseEntity);
			GlobalResponseBean grb= new GlobalResponseBean();
			GlobalErrorBean err = new GlobalErrorBean();
			
			if (responseEntity.getStatusCode() != HttpStatus.OK) {
				grb.setStatusCode("-3000");
				err.setErrorDescription(responseEntity.getStatusCode()+ " Error in calling WSGService");
				err.setErrorDetail(responseEntity.toString());
				grb.setObject(err);
				return grb;
			}
			grb.setStatusCode("0");
			UserProfiles userProfiles = responseEntity.getBody();
			logger.debug("userProfiles:"+userProfiles);
			//setting Role value for the response
			response.setRole(userProfiles.getRoles().getRole());
		} catch (Exception ex) {
			logger.error("Could not parse the output", ex);
			return new GlobalResponseBean("-3001" , ex.getMessage());
		}

		return new GlobalResponseBean("0" , "Success");
	}	
}
