package com.tmobile.federation.dao;



import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tmobile.federation.beans.req.Enterprise;
import com.tmobile.federation.beans.res.EnterpriseResponse;
import com.tmobile.federation.config.DatabaseConfig;
import com.tmobile.federation.utils.FederationAppConfig;



public class EnterpriseImpl implements EnterpriseDAO {
	
private  final String CREATED_BY=FederationAppConfig.getCreatedBy();
private final String LOGO_URL_TYPE=FederationAppConfig.getLogoURLType();

private final int IDX_STATUS_CODE = 1;
private final int IDX_STATUS_DESC = 2;
private final int IDX_TENANT_ID = 3;
private final int IDX_DOMAIN = 4;
private final int IDX_ENTERPRISE_NAME = 5;
private final int IDX_AUTO_ON_BOARDING = 6;
private final int IDX_MAX_USERS_IDS =7;
private final int IDX_PROVIDER = 8 ;
private final int IDX_CHECK_PROVIDER = 9;

private final int IDX_ADMIN = 10;
private final int IDX_EMAIL = 11;
private final int IDX_CONTACT_NUMBER = 12;
private final int IDX_ROLE = 13;
private final int IDX_CREATE_TENANT_URI=14;
private final int IDX_LOGO_URI_TYPE=15;
private final int IDX_LOGO_URL=16;
private final int IDX_CREATED_BY = 17;

private static final Logger logger = Logger.getLogger(EnterpriseImpl.class);


	public EnterpriseImpl() {
		
	}
 
	@Override
	public EnterpriseResponse createEnterprise(Enterprise enterprise) {
		EnterpriseResponse resp=new EnterpriseResponse();
		String auto_onboarding="N";
		String federationProvider="";
		String checkProvider="N";
		String createTenantURI="N";
		CallableStatement callableStatement=null;

		Connection databaseConnection = DatabaseConfig.getDataConnection();

		try {
			if (enterprise.getFederationInfo()!=null) {
				if (enterprise.getFederationInfo().getAutoUserOnboardingEnabled()) {
					auto_onboarding="Y";
				} else {
					auto_onboarding="N";
				}
				federationProvider=enterprise.getFederationInfo().getFederationProvider();
				checkProvider="Y";
			}
			
			if (checkProvider.equals("Y") && !enterprise.getLogoUrl().equals(null) &&
					!enterprise.getLogoUrl().equals("")) {
			    createTenantURI="Y";
			};
			databaseConnection.setAutoCommit(false);
			callableStatement =         
					databaseConnection.prepareCall("{call check_enterprise_v2(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			callableStatement.registerOutParameter(IDX_STATUS_CODE,Types.INTEGER);
			callableStatement.registerOutParameter(IDX_STATUS_DESC,Types.VARCHAR);
			callableStatement.setString(IDX_TENANT_ID,enterprise.getCorpNodeID());
			callableStatement.setString(IDX_DOMAIN,enterprise.getDomain());
			callableStatement.setString(IDX_ENTERPRISE_NAME,enterprise.getEnterpriseName());
			callableStatement.setString(IDX_AUTO_ON_BOARDING,auto_onboarding);
			
			callableStatement.setString(IDX_MAX_USERS_IDS,enterprise.getMaxUserIds());
			
			callableStatement.setString(IDX_PROVIDER,federationProvider);
			callableStatement.setString(IDX_CHECK_PROVIDER,checkProvider);
			callableStatement.setString(IDX_ADMIN,enterprise.getAdminInfo().getFirstName()+' '+ enterprise.getAdminInfo().getLastName());
			callableStatement.setString(IDX_EMAIL,enterprise.getAdminInfo().getEmailAddress());
			callableStatement.setString(IDX_CONTACT_NUMBER,enterprise.getAdminInfo().getContactNumber());
			callableStatement.setString(IDX_ROLE,enterprise.getAdminInfo().getRole());
			
			callableStatement.setString(IDX_CREATE_TENANT_URI,createTenantURI);
			callableStatement.setString(IDX_LOGO_URI_TYPE,LOGO_URL_TYPE);
			callableStatement.setString(IDX_LOGO_URL,enterprise.getLogoUrl());
			callableStatement.setString(IDX_CREATED_BY,CREATED_BY);
			
			callableStatement.executeUpdate();
			resp.setStatusCode(callableStatement.getInt(1));
			resp.setShortDesc(callableStatement.getString(2));
			if (callableStatement.getInt(1)!=0) {
				return resp;
			}
		   
			databaseConnection.commit();
		
			
		} catch (SQLException e) {
			resp.setStatusCode(-5000);
			resp.setShortDesc("CreateEnterprise: DB Internal Error");
			resp.setLongDesc(e.getMessage());
			logger.error(e.getMessage());
			if (databaseConnection !=null) {
				try {
					databaseConnection.rollback();
				} catch(SQLException e1) {
					logger.error(e1.getMessage());
				}
			}
			
			e.printStackTrace();
		} finally {
		   closeDbConnection(databaseConnection,callableStatement);
		   
		}
		return resp;
	}

	private void closeDbConnection(Connection databaseConnection, CallableStatement callableStatement) {
		if (callableStatement!=null) {
			   try {
				   callableStatement.close();
			   }   catch (Exception any) {
					   
			   }
		   }
	   if (databaseConnection!=null){
		   try {
			   databaseConnection.close();
		   } catch(Exception any) {
		   }	   
		   
	   }
	}

	@Override
	public void rollBackEnterprise(Enterprise enterprise) {
		EnterpriseResponse resp=new EnterpriseResponse();
		Connection databaseConnection = null; 
		CallableStatement callableStatement=null;

	try {	
		databaseConnection=DatabaseConfig.getDataConnection();
		callableStatement = databaseConnection.prepareCall("{call rollback_enterprise(?,?,?)}");
		callableStatement.registerOutParameter(1,Types.INTEGER);
		callableStatement.registerOutParameter(2,Types.VARCHAR);
		callableStatement.setString(3, enterprise.getCorpNodeID());
		callableStatement.executeUpdate();
		//databaseConnection.commit();
		if (callableStatement.getInt(1)!=0) {
			resp.setStatusCode(callableStatement.getInt(1));
			resp.setShortDesc(callableStatement.getString(2));
			return;
		}
	} catch (SQLException e) {
		resp.setStatusCode(-5000);
		resp.setShortDesc("Internal Error");
		logger.debug(e.getMessage());
		if (databaseConnection !=null) {
			try {
				databaseConnection.rollback();
			} catch(SQLException e1) {
			}
		}
	} finally {
		   closeDbConnection(databaseConnection,callableStatement);
	}
    return;	
	
}

	/*    
    public static Connection getDataSourceConnection() {
    	Context ctx=null;
    	Connection conn=null;
    	Hashtable ht = new Hashtable();
    	ht.put(Context.INITIAL_CONTEXT_FACTORY,
    			"weblogic.jndi.WLInitialContextFactory");
    	ht.put(Context.PROVIDER_URL, "t3://localhost:7001");
    	try {
    		ctx=new InitialContext(ht);
    		DataSource ds = (DataSource) ctx.lookup("jdbc0");
    		conn=ds.getConnection();
    		return conn;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return null;
    	
    }	
 */   

}
