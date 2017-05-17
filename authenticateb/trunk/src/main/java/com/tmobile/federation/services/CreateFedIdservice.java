package com.tmobile.federation.services;

import com.tmobile.federation.beans.req.FedIdRequestBean;
import com.tmobile.federation.beans.res.GlobalErrorBean;
import com.tmobile.federation.beans.res.GlobalResponseBean;
import com.tmobile.federation.beans.validators.FedIdRequestValidator;
import com.tmobile.federation.dao.IAMUserDAO;
import com.tmobile.iam.federation.services.IAMFederationProfileService;
import com.tmobile.iam.federation.services.exceptions.IAMCreateEmptyProfileException;
import com.tmobile.wsg.services.WSGCDBProfileService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

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
public class CreateFedIdservice implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(CreateFedIdservice.class);

    @Autowired private IAMUserDAO iAMUserDAO;

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
    public GlobalResponseBean createFedId(@RequestBody FedIdRequestBean fedIdRequestBean, HttpServletRequest request) {
        logger.info(fedIdRequestBean.toString());
        String authorizationHeader = request != null ? request.getHeader("authorization") : null;
        if (null == authorizationHeader || authorizationHeader
          .trim()
          .isEmpty()) {
            logger.error("Authorization Header not found");
            return new GlobalResponseBean("401 Unauthorized", null);
        }
        String authToken[] = authorizationHeader.split(":");
        if (authToken.length != 2) {
            logger.error("Invalid Authorization Header");
            return new GlobalResponseBean("401 Unauthorized", null);
        }
        String user = authToken[0];
        String pass = authToken[1];
        if (user == null || user
          .trim()
          .isEmpty()) {
            logger.error("Invalid Authorization Header");
            return new GlobalResponseBean("401 Unauthorized", null);
        }
        if (pass == null || pass
          .trim()
          .isEmpty()) {
            logger.error("Invalid Authorization Header");
            return new GlobalResponseBean("401 Unauthorized", null);
        }
        String validation = FedIdRequestValidator.validateFedIdRequestBean(fedIdRequestBean);
        if (validation != null) {
            GlobalErrorBean errorBean = new GlobalErrorBean();
            errorBean.setErrorDescription("Missing mandatory attribute");
            errorBean.setErrorDetail(validation);
            logger.error(errorBean.getErrorDescription());
            return new GlobalResponseBean("400 Bad Request", errorBean);
        } else {
            String result = iAMUserDAO.createFederatedId(fedIdRequestBean.getIamUserId(), fedIdRequestBean.getTenantId(), fedIdRequestBean.getEmail(), user);
            logger.info("Procedure Response is : " + result);
            if (result == null) {
                return new GlobalResponseBean("400 Bad Request", null);
            }
            try {
                IAMFederationProfileService iamFederationProfileService = new IAMFederationProfileService();
                iamFederationProfileService.createEmptyProfile(request.getRequestURI());
            } catch (IAMCreateEmptyProfileException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return new GlobalResponseBean("400 Bad Request", null);
            }
            //Send response to user
            FedIdRequestBean fedIdRequestBeanForResponse = new FedIdRequestBean();
            fedIdRequestBeanForResponse.setTenantId(fedIdRequestBean.getTenantId());
            fedIdRequestBeanForResponse.setEmail(fedIdRequestBean.getEmail());
            fedIdRequestBeanForResponse.setIamUserId(fedIdRequestBean.getIamUserId());
            return new GlobalResponseBean("201 Created", fedIdRequestBeanForResponse);
        }
    }

    /**
     *
     * @param request
     *
     * @return
     */
    @RequestMapping(value = "/provisioning/v1/fedId", method = RequestMethod.GET)
    public GlobalResponseBean getFedId(@RequestParam(required = false) String iamUserId, @RequestParam(required = false) String email, @RequestParam(required = false) String tenantId, HttpServletRequest request) {
        String authorizationHeader = request != null ? request.getHeader("authorization") : null;
        String transactionId = request != null ? request.getHeader("transactionId") : null;
        System.out.println("authorizationHeader:" + authorizationHeader + " transactionId:" + transactionId + "iAMUserDAO Instance:" + iAMUserDAO + " iamUserId:" + iamUserId + " email:" + email + " tenantId:" + tenantId);
        logger.debug("authorizationHeader:" + authorizationHeader + " transactionId:" + transactionId + "iAMUserDAO Instance:" + iAMUserDAO);
        FedIdRequestBean response = new FedIdRequestBean();
        if (null == authorizationHeader || authorizationHeader
          .trim()
          .isEmpty()) {
            logger.error("Authorization Header not found");
            return new GlobalResponseBean(HttpStatus.UNAUTHORIZED.value() + " " + HttpStatus.UNAUTHORIZED.getReasonPhrase(), null);
        }
        String authToken[] = authorizationHeader.split(":");
        if (authToken.length != 2) {
            logger.error("Invalid Authorization Header");
            return new GlobalResponseBean(HttpStatus.UNAUTHORIZED.value() + " " + HttpStatus.UNAUTHORIZED.getReasonPhrase(), null);
        }
        String user = authToken[0];
        String pass = authToken[1];
        if (user == null || user
          .trim()
          .isEmpty()) {
            logger.error("Invalid Authorization Header");
            return new GlobalResponseBean(HttpStatus.UNAUTHORIZED.value() + " " + HttpStatus.UNAUTHORIZED.getReasonPhrase(), null);
        }
        if (pass == null || pass
          .trim()
          .isEmpty()) {
            logger.error("Invalid Authorization Header");
            return new GlobalResponseBean(HttpStatus.UNAUTHORIZED.value() + " " + HttpStatus.UNAUTHORIZED.getReasonPhrase(), null);
        }
        if (transactionId != null) {
            logger.info("Transaction id=" + transactionId);
        } else {
            logger.info("Transaction Id is not given");
        }
        String validation = FedIdRequestValidator.validateFedIdRequest(email, iamUserId, tenantId);
        if (validation != null) {
            logger.info("validateFedIdRequest(email, iamUserId, tenantId) is failure");
            return new GlobalResponseBean(HttpStatus.BAD_REQUEST.value() + " " + HttpStatus.BAD_REQUEST.getReasonPhrase(), getGlobalErrorForMandatoryRows(validation));
        }

        String sqlResponse = iAMUserDAO.getFederatedId(iamUserId, email, tenantId);
        if (sqlResponse.equals("USER")) {
            return new GlobalResponseBean(HttpStatus.BAD_REQUEST.value() + " " + HttpStatus.BAD_REQUEST.getReasonPhrase(), "User Does Not Exists");
        } else if (sqlResponse.equals("TENANT")) {
            return new GlobalResponseBean(HttpStatus.BAD_REQUEST.value() + " " + HttpStatus.BAD_REQUEST.getReasonPhrase(), "Tenant Does Not Exists");
        } else if (sqlResponse != null && !sqlResponse
          .trim()
          .isEmpty()) {
            logger.info("setFederatedUserId =" + sqlResponse);
            response.setFederatedUserId(sqlResponse);
        }
        if (tenantId != null && !tenantId
          .trim()
          .isEmpty()) {
            response.setTenantId(tenantId);
        }
        response.setIamUserId(iamUserId);
        response.setEmail(email);

        //tenantUID =iamUserId@tenant_realm from the db
        String tenantUID = iamUserId + "@" + iAMUserDAO.getTenantRealmAndStatus(response);
        WSGCDBProfileService wsgcdbProfileService = new WSGCDBProfileService();
        //role will be set after the below function
        wsgcdbProfileService.getCDBUserProfile(tenantUID, response);
        return new GlobalResponseBean(HttpStatus.OK.value() + " " + HttpStatus.OK.getReasonPhrase(), response);
    }

    private GlobalErrorBean getGlobalErrorForMandatoryRows(String error) {
        GlobalErrorBean errorBean = new GlobalErrorBean();
        errorBean.setErrorDescription("Missing mandatory parameter");
        errorBean.setErrorDetail(error);
        logger.error(errorBean.getErrorDescription());
        return errorBean;
    }

    public void setIAMUserDAO(IAMUserDAO iamUserDAO) {
        this.iAMUserDAO = iamUserDAO;
    }

}
