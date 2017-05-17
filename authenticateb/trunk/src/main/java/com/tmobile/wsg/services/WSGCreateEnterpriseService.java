package com.tmobile.wsg.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.tmobile.federation.beans.req.Enterprise;
import com.tmobile.federation.beans.res.EnterpriseResponse;
import com.tmobile.federation.utils.RequestBuilder;
import com.tmobile.iam.federation.services.exceptions.IAMCreateEmptyProfileException;
import com.tmobile.iam.fedetaion.services.req.beans.Header;
import com.tmobile.wsg.services.req.beans.CustomerProfile;
import com.tmobile.wsg.services.req.beans.generated.Control;
import com.tmobile.wsg.services.req.beans.generated.CustomProfile;
import com.tmobile.wsg.services.req.beans.generated.ListDef;
import com.tmobile.wsg.services.req.beans.generated.ListItemDef;
import com.tmobile.wsg.services.req.beans.generated.Roles;
import com.tmobile.wsg.services.req.beans.generated.Service;
import com.tmobile.wsg.services.req.beans.generated.Services;
import com.tmobile.federation.beans.req.Feature;
import com.tmobile.federation.beans.req.Attribute;


public class WSGCreateEnterpriseService extends WSGBaseFederationService {
	private static final Logger logger = Logger.getLogger(WSGCreateEnterpriseService.class);

	public EnterpriseResponse createEnterpriseService(String caller,Enterprise enterprise)  {

		logger.debug(getCreateEnterpriseUrl() + " " + caller);
		
		ObjectMapper om = new ObjectMapper();
		Object resObj = null;
		
		try {
			
			List<Header> headers = new ArrayList<Header>();
			
			CustomProfile cp = new CustomProfile();
			
			ListItemDef domainUrl = new ListItemDef();
			domainUrl.setEntryUri(enterprise.getDomain());
			ListItemDef corpId = new ListItemDef();
			corpId.setEntryUri(enterprise.getCorpNodeID());
			
            ListDef listDef = new ListDef() ;
            listDef.getListItem().add(domainUrl);
            listDef.getListItem().add(corpId);
            
            cp.setKeyList(listDef);
			//banId is optional but here minimum is 1???
			if (enterprise.getBanlist()!=null) {
				cp.getBanId().addAll(Arrays.asList(enterprise.getBanlist()));
			
			}
		
			cp.setEntid(enterprise.getCorpNodeID());
			if (enterprise.getEnterpriseName()!=null) {
				cp.setDisplayName(enterprise.getEnterpriseName());
			};
			cp.setTenantDomain(enterprise.getDomain());
		
			cp.setMaxUserIds(new BigInteger(enterprise.getMaxUserIds())); 
			if (enterprise.getLogoUrl()!=null) {
				cp.setLogoUrl(enterprise.getLogoUrl());
			}
			
			if (enterprise.getEnterpriseFeatures()!=null) {
				Services services = new Services();
				for (Feature ftr : enterprise.getEnterpriseFeatures() ) {
					Service service = new Service();
					service.setName(ftr.getFeaturename());
					if (ftr.getAttributes()!=null){
						for (Attribute attribute : ftr.getAttributes() ) {
							Control control =new Control();
							control.setName(attribute.getName());
							control.setValue(attribute.getValue());
							service.getControl().add(control);
						}
					}	
				
					services.getService().add(service);
				}
				cp.setServices(services);
			}
			if (enterprise.getEnterpriseRole()!=null) {
				Roles roles = new Roles();
				roles.getRole().add(enterprise.getEnterpriseRole().getRole());
				cp.setRoles(roles);
				
			}
			
		//	CustomerProfile customerProfile= new CustomerProfile();
	//		customerProfile.setCustomProfile(cp);
			
			
			
			String jsonString = om.writeValueAsString(cp) ;

		    Gson gson = new Gson();
		    JsonElement je = gson.toJsonTree(cp);
		    JsonObject jo = new JsonObject();
		    jo.add("customProfile", je);
		 //   System.out.println(jo.toString());	
		    
			logger.debug("Json request " + jo.toString());

			final BasicCookieStore cookieStore = new BasicCookieStore();
			HttpResponse<String> response = RequestBuilder.send(cookieStore,
															    "iamTimeout",
															    "PUT",
															    getCreateEnterpriseUrl()+"/"+caller, 
															    headers, 
															    null,
															    jo.toString(),
															  	"authenticateb -> wsg -> createEnterprise -> " + caller);
			
			String output = null;
			if(response != null) {
				output = response.getBody();
			}
			
			logger.debug("Output from Server .... \n");
			logger.debug(output);
			EnterpriseResponse erp= new EnterpriseResponse();
			
			if (response.getStatus() != Status.OK.getStatusCode()) {
				erp.setStatusCode(-3000);
				erp.setShortDesc(response.getStatus()+ "Error in calling WSGService");
				erp.setLongDesc(StringEscapeUtils.escapeJson(output));
				return erp;
			}
			erp.setStatusCode(0);
			resObj =  om.readValue(output, Object.class);
			logger.debug(resObj.toString());
		} catch (Exception ex) {
			logger.error("Could not parse the output", ex);
			return new EnterpriseResponse(-3001 , ex.getMessage());
		}

		return new EnterpriseResponse(0 , "Success");
	}	
}
