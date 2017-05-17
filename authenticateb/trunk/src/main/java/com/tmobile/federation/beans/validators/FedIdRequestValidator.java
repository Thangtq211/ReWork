package com.tmobile.federation.beans.validators;


import org.apache.commons.validator.routines.EmailValidator;

import com.tmobile.federation.beans.req.FedIdRequestBean;

/**
 * 
 * @author Admin
 *
 */

public class FedIdRequestValidator {

	
	/**
	 * 
	 * @param fedIdRequestBean
	 * 
	 * Validate FedIdRequestBean
	 * email, tenantId, status and role are mandatory fields
	 * 
	 * @return
	 */
	public static String validateFedIdRequestBean(FedIdRequestBean fedIdRequestBean){
		if(fedIdRequestBean.getEmail() == null || fedIdRequestBean.getEmail().trim().isEmpty()){
			return "email";
		}else if(!EmailValidator.getInstance().isValid(fedIdRequestBean.getEmail())){
			return "email";
		}
		if(fedIdRequestBean.getTenantId() == null || fedIdRequestBean.getTenantId().trim().isEmpty()){
			return "tenantId";
		}
		if(fedIdRequestBean.getStatus() == null || fedIdRequestBean.getStatus().trim().isEmpty()){
			return "status";
		}
		if(fedIdRequestBean.getRole() == null || fedIdRequestBean.getRole().trim().isEmpty()){
		 return "role";
		}
		return null;
	}


    public static String validateFedIdRequest(String email, String iamUserId, String tenantId){
        if(iamUserId == null && email == null){
            return "email or iamuserid";
        }else if(tenantId  == null){
            return "tenantId";
        }
        else return null;
    }
}

		
