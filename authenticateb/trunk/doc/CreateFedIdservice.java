package com.tmobile.federation.services;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tmobile.federation.beans.req.FedIdRequestBean;
import com.tmobile.federation.beans.res.GlobalErrorBean;
import com.tmobile.federation.beans.res.GlobalResponseBean;
import com.tmobile.federation.beans.validators.FedIdRequestValidator;
import com.tmobile.federation.dao.IAMUserDAO;
import com.tmobile.iam.federation.services.IAMFederationProfileService;
import com.tmobile.iam.federation.services.exceptions.IAMCreateEmptyProfileException;

/**
 * 
 * @author Admin
 * 
 * @since 1.0
 *
 *
 * This service supposed to provide end point for fedId services
 */
 

@RestController
public class CreateFedIdservice implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(CreateFedIdservice.class);
	
	@Autowired
	private IAMUserDAO iAMUserDAO;
	
	
	/**
	 * 
	 * @param fedIdRequestBean
	 * @param request
	 * 
	 * To consume this service authorization request header is mandatory;
	 * In request object email, tenantId, status and role are mandatory fields
	 * So any request without mandatory parameters will not be served.
	 * In this case proper error message will be returned to consumers
	 * 
	 * @return
	 */
	@RequestMapping(value = "/provisioning/v1/fedId", method = RequestMethod.POST)
	public GlobalResponseBean createFedId(@RequestBody FedIdRequestBean fedIdRequestBean, HttpServletRequest request){
		logger.info(fedIdRequestBean.toString());
		String authorizationHeader = request!=null?request.getHeader("authorization"):null;
		if(null == authorizationHeader || authorizationHeader.trim().isEmpty()){
			logger.error("Authorization Header not found");
			return new GlobalResponseBean("401 Unauthorized", null);
		}
		String authToken[] = authorizationHeader.split(":");
		if(authToken.length != 2){
			logger.error("Invalid Authorization Header");
			return new GlobalResponseBean("401 Unauthorized", null);
		}
		String user = authToken[0];
		String pass = authToken[1];
		if(user == null || user.trim().isEmpty()){
			logger.error("Invalid Authorization Header");
			return new GlobalResponseBean("401 Unauthorized", null);
		}
		if(pass == null || pass.trim().isEmpty()){
			logger.error("Invalid Authorization Header");
			return new GlobalResponseBean("401 Unauthorized", null);
		}
		String validation = FedIdRequestValidator.validateFedIdRequestBean(fedIdRequestBean);
		if(validation != null){
			GlobalErrorBean errorBean = new GlobalErrorBean();
			errorBean.setErrorDescription("Missing mandatory attribute");
			errorBean.setErrorDetail(validation);
			logger.error(errorBean.getErrorDescription());
			return new GlobalResponseBean("400 Bad Request", errorBean);
		} else {
			String result = iAMUserDAO.createFederatedId(fedIdRequestBean.getIamUserId(), fedIdRequestBean.getTenantId(), fedIdRequestBean.getEmail(), user);
			logger.info("Procedure Response is : "+ result);
			if(result == null){
				return new GlobalResponseBean("400 Bad Request", null);
			}
			try {
				IAMFederationProfileService iamFederationProfileService = new IAMFederationProfileService();
				iamFederationProfileService.createEmptyProfile(request.getRequestURI());
			} catch (IAMCreateEmptyProfileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Send response to user
			FedIdRequestBean fedIdRequestBeanForResponse = new FedIdRequestBean();
			fedIdRequestBeanForResponse.setTenantId(fedIdRequestBean.getTenantId());
			fedIdRequestBeanForResponse.setEmail(fedIdRequestBean.getEmail());
			fedIdRequestBeanForResponse.setIamUserId(fedIdRequestBean.getIamUserId());
			return new GlobalResponseBean("201 Created", fedIdRequestBeanForResponse);
		}
	}

}
