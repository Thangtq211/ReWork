package com.tmobile.federation.beans.req.userProfile;

import javax.servlet.http.HttpServletRequest;

import com.tmobile.federation.services.AuthenticationService;
import com.tmobile.federation.services.CreateFedIdservice;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tmobile.federation.beans.req.Enterprise;
import com.tmobile.federation.beans.req.EnterpriseRequest;
import com.tmobile.federation.beans.req.FedIdRequestBean;
import com.tmobile.federation.beans.res.EnterpriseResponse;
import com.tmobile.federation.beans.res.GlobalErrorBean;
import com.tmobile.federation.beans.res.GlobalResponseBean;
import com.tmobile.federation.dao.EnterpriseDAO;
import com.tmobile.federation.dao.EnterpriseDAOImpl;
import com.tmobile.federation.utils.FederationAppConfig;
import com.tmobile.federation.utils.Utils;
import com.tmobile.wsg.services.WSGCreateEnterpriseService;

@RestController
public class EnterpriseService {
	private static final Logger logger = Logger.getLogger(EnterpriseService.class);

	
	//@Autowired
	//private EnterpriseDAO enterpriseDao ;
	
	@RequestMapping(value = "/getEnterprise", method = RequestMethod.GET)
	public  EnterpriseResponse getEnterprise() {
		EnterpriseResponse erp= new EnterpriseResponse();
        erp.setStatusCode(0);
        erp.setShortDesc("Success");
        
        return erp;
		
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/createEnterprise", method = RequestMethod.POST,
	 consumes = MediaType.APPLICATION_JSON_VALUE,
     produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <GlobalErrorBean> createEnterprise(@RequestBody EnterpriseRequest enterpriseRequest,
			 HttpServletRequest request) {
		GlobalErrorBean eb=new GlobalErrorBean();

		String authorizationHeader = request.getHeader("authorization");
		
	/*	if (!authorizationHeader.equalsIgnoreCase("Basic Base64(userNAme:password)")) {
				return new ResponseEntity <GlobalErrorBean> (eb, HttpStatus.UNAUTHORIZED); 
		}*/
		
		AuthenticationService authenticationService = new AuthenticationService();

		boolean authenticationStatus = authenticationService
				.authenticate(authorizationHeader);
		
		if (!authenticationStatus) {

			return new ResponseEntity <GlobalErrorBean> (eb, HttpStatus.UNAUTHORIZED); 
		}
		

		

	if (enterpriseRequest==null) {
		eb.setErrorDescription("No requestbody received");
    	return new ResponseEntity <GlobalErrorBean> (eb, HttpStatus.BAD_REQUEST); 
	}
	
	Enterprise enterprise = enterpriseRequest.getEnterpriseProfile();
	
	if (enterpriseRequest.getEnterpriseProfile()==null) {
		eb.setErrorDescription("Missing mandatory field.");
		eb.setErrorDetail("enterpriseProfile");
    	return new ResponseEntity <GlobalErrorBean> (eb, HttpStatus.BAD_REQUEST); 
	
	}	
		
	EnterpriseResponse erp = new EnterpriseResponse();
	EnterpriseDAO enterpriseDao = new EnterpriseDAOImpl();
		
		
	erp=validateRequiredField(enterprise);
	    
	    if (erp.getStatusCode()==0) {
	    	
	    	erp=enterpriseDao.createEnterprise(enterprise);
	    	if (erp.getStatusCode()==0) {
	    		  erp=callCreateFedIdMethod(enterprise,request);
	    		  if (erp.getStatusCode()==0) {
	    			  erp=callWSGService(enterprise);
	    			  if (erp.getStatusCode()!=0) {
	    				  enterpriseDao.rollBackEnterprise(enterprise); 
	    			  }	  
	    		  } else {
		    		   enterpriseDao.rollBackEnterprise(enterprise); 
	    		  }
	    	} 
	    	
	    	
	    } 
	    /*  ??Need to clarify the standard response requirements   */
	eb.setErrorDescription(erp.getShortDesc());  
	eb.setErrorDetail(erp.getLongDesc());
    logger.debug(erp.toString());
	    
  if (erp.getHttpResponseCode()==400){
	  return new ResponseEntity <GlobalErrorBean> (eb, HttpStatus.BAD_REQUEST); 
   } else if (erp.getStatusCode()==0){
	   eb.setErrorDescription("Success");
	   eb=null;
   } else {
	   //NEED TO MAP all the httpResponseCode return from other service
	   //Map this later
   }	   
	
	
	return new ResponseEntity<GlobalErrorBean>(eb, HttpStatus.OK); 

	}

	

	private EnterpriseResponse callWSGService(Enterprise enterprise) {
	  	
	   	WSGCreateEnterpriseService wsgCreateEnterpriseService = new WSGCreateEnterpriseService();
	   return 	wsgCreateEnterpriseService.createEnterpriseService(enterprise.getCorpNodeID(),enterprise);
		
	}

	private EnterpriseResponse callCreateFedIdMethod(Enterprise enterprise, HttpServletRequest request) {
		
		logger.debug("Calling createFedId...");
		FedIdRequestBean fedIdRequestBean = new FedIdRequestBean() ;
		fedIdRequestBean.setEmail(enterprise.getAdminInfo().getEmailAddress());
		fedIdRequestBean.setTenantId(enterprise.getCorpNodeID());
		fedIdRequestBean.setStatus("ACTIVE");
		fedIdRequestBean.setRole(enterprise.getAdminInfo().getRole());//SuperAdmin
        if (enterprise.getBanlist()!=null) {
			fedIdRequestBean.setBanList(enterprise.getBanlist());
        }	
        
        if (enterprise.getEnterpriseFeatures()!=null) {
        	fedIdRequestBean.setFeatures(enterprise.getEnterpriseFeatures());
        }  
		if (enterprise.getenterpriseSOC()!=null) {
			fedIdRequestBean.setBusinessSOC(enterprise.getenterpriseSOC());
		}
		
	    CreateFedIdservice fedId = new CreateFedIdservice();
		GlobalResponseBean credFedIdResp = fedId.createFedId(fedIdRequestBean, request);
		logger.debug("called and response is " + credFedIdResp.getStatusCode());
	    EnterpriseResponse erp= new EnterpriseResponse();
		erp.setHttpResponseCode(Integer.parseInt(credFedIdResp.getStatusCode().substring(0,3)));
		
		 
	    if (erp.getHttpResponseCode()==201) {
	    	erp.setStatusCode(0);
	    	//Successful
	    } else {
	    	erp.setStatusCode(-1000);
	    	if (credFedIdResp.getObject()!=null)  {
	    		GlobalErrorBean eb = (GlobalErrorBean)credFedIdResp.getObject();
	    		erp.setShortDesc(eb.getErrorDescription());
	    		erp.setLongDesc(eb.getErrorDetail());
	    	}	else {
	    		erp.setShortDesc("CreateFedId failed");
	    	}	
	    }	
	    
		return erp;
	}


	private  EnterpriseResponse validateRequiredField(Enterprise enterprise) {
		EnterpriseResponse erp = new EnterpriseResponse();
		
		/*ENTERPRISE section */
		erp.setHttpResponseCode(400);
		
		/* Commented because its not mandatory
		if (enterprise.getEnterpriseName()==null ||
				enterprise.getEnterpriseName().equals("")) {
			erp.setStatusCode(-101);
			erp.setShortDesc("Missing mandatory field.");
			erp.setLongDesc("enterpriseName");
			return erp;
		}
		*/
		
		if (enterprise.getCorpNodeID()==null||
				enterprise.getCorpNodeID().equals("")) {
			erp.setStatusCode(-102);
			erp.setShortDesc("Missing mandatory field.");
			erp.setLongDesc("corpNodeID");
			return erp;
		}
		
		if ( enterprise.getDomain()==null || enterprise.getDomain().equals("")) {
			enterprise.setDomain(Utils.generateRandomString(FederationAppConfig.getMaxDomainLength())+
					".t-mobile.com");
		} 
		
		if (enterprise.getAdminInfo()==null) {
			erp.setStatusCode(-300);
			erp.setShortDesc("Missing mandatory field.");
			erp.setLongDesc("adminInfo");
			return erp;
		} else {
			if (enterprise.getAdminInfo().getEmailAddress()==null ||  
					enterprise.getAdminInfo().getEmailAddress().equals(""))  {
				erp.setStatusCode(-301);
				erp.setShortDesc("Missing mandatory field.");
				erp.setLongDesc("emailAddress");
				return erp;
			}			
		}
		
		
		if (enterprise.getLogoUrl()!=null &&
				!enterprise.getLogoUrl().equals("")) {
			
			/*if logourl is present it needs fed_type_id for it to be created */
			if (enterprise.getFederationInfo()==null) {
				erp.setStatusCode(-200);
				erp.setShortDesc("Missing mandatory field.");
				erp.setLongDesc("federationInfo");
				return erp;
			}	
			if (!Utils.isValidURL(enterprise.getLogoUrl())) {
				erp.setStatusCode(-106);
				erp.setShortDesc("URL is invalid");
				erp.setLongDesc("value :" + enterprise.getLogoUrl());
				return erp;
			}	
		}
		
		if (enterprise.getFederationInfo()!=null) {
			
			if (enterprise.getFederationInfo().getFederationEnabled()==null ||
					enterprise.getFederationInfo().getFederationEnabled().equals("")) {
				erp.setStatusCode(-201);
				erp.setShortDesc("Missing mandatory field.");
				erp.setLongDesc("federationEnabled");
				return erp;
			}
			if (enterprise.getFederationInfo().getFederationProvider()==null ||
					enterprise.getFederationInfo().getFederationProvider().equals("")) {
				erp.setStatusCode(-202);
				erp.setShortDesc("Missing mandatory field.");
				erp.setLongDesc("federationProvider should not be empty because LogoURL is defined.");
				return erp;
			} 
	
		} 
		
		/*Ban List Section*/
        
		logger.debug("Checking maxuserids");
		if (enterprise.getMaxUserIds()==null || enterprise.getMaxUserIds().equals("")) {
			enterprise.setMaxUserIds(FederationAppConfig.getDefaultMaxUsers());
		} else {
			try {
				Integer.parseInt(enterprise.getMaxUserIds());
				
				
			} catch (Exception ex) {
				erp.setStatusCode(107);
				erp.setShortDesc("Mandatory value is invalid.");
				erp.setLongDesc("maxUserIds is optional but if defined, it should be numeric:"
				   +enterprise.getMaxUserIds());
				
				return erp;
			}
		}
		
		erp.setStatusCode(0);
		
		return erp;
	}

}
