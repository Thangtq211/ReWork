package com.tmobile.federation.dao;

import com.tmobile.federation.beans.req.Enterprise;
import com.tmobile.federation.beans.res.EnterpriseResponse;


public interface EnterpriseDAO {

	EnterpriseResponse createEnterprise(Enterprise enterprise);
	void rollBackEnterprise(Enterprise enterprise);
	
	
}
