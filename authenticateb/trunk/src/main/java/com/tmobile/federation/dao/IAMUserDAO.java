package com.tmobile.federation.dao;

import com.tmobile.federation.beans.req.FedIdRequestBean;

/**
 * 
 * @author Admin
 * 
 * @since 1.0
 */

public interface IAMUserDAO {
	
	
	/**
	 * 
	 * @param email
	 * @param userId
	 * @param tenantID
	 * @param createdBy
	 */
	String createFederatedId(String email, String userId, String tenantID, String createdBy);


    /**
     *
     * @param iamUserId
     * @param email
     * @param tenantId
     * @return
     */
	String getFederatedId(String iamUserId, String email, String tenantId);

	/**
	 *
	 * @param response
	 * @return
	 */
	String getTenantRealmAndStatus(FedIdRequestBean response);
}
