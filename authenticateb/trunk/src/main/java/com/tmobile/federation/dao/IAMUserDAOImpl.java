package com.tmobile.federation.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Logger;

import com.tmobile.federation.beans.req.FedIdRequestBean;
import com.tmobile.wsg.services.WSGCDBProfileService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tmobile.federation.config.DatabaseConfig;


/**
 * 
 * @author Admin
 *
 *
 * @since 1.0
 */
@Service
public class IAMUserDAOImpl implements IAMUserDAO{
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(IAMUserDAOImpl.class);
	private Connection databaseConnection;
	
	public IAMUserDAOImpl() {
		logger.debug("Constructor");
		this.databaseConnection = DatabaseConfig.getDataConnection();
	}

	@Override
	public String createFederatedId(String email, String userId, String tenantID, String createdBy) {
		try {
			if(DatabaseConfig.getDataConnection() == null){
				logger.error("Database connection not found");
			}
			CallableStatement callableStatement = databaseConnection.prepareCall("{call fbi_stg_tenant_user_chk(?,?,?,?,?)}");
			callableStatement.setString(1, userId);
			callableStatement.setString(2, tenantID);
			callableStatement.setString(3, email);
			callableStatement.setString(4, createdBy);
			callableStatement.registerOutParameter(5, Types.VARCHAR);
			callableStatement.executeUpdate();
			closeDataBaseConnection();
			String result = callableStatement.getString(5);
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeDataBaseConnection();
		}
		return null;
	}

    /**
     * @param iamUserId
     * @param email
     * @param tenantId
     * @return
     */
    @Override
    public String getFederatedId(String iamUserId, String email, String tenantId) {
        try {
        	logger.debug("getting FederatedId with iamUserId:"+iamUserId);
            if(DatabaseConfig.getDataConnection() == null){
				logger.error("Database connection not found");
            }
            CallableStatement callableStatement = databaseConnection.prepareCall("{call fbi_stg_user_fed_id(?,?,?,?)}");
            callableStatement.setString(1, iamUserId);
            callableStatement.setString(2, email);
            callableStatement.setString(3, tenantId);
            callableStatement.registerOutParameter(4, Types.VARCHAR);
            callableStatement.executeUpdate();
            String result = callableStatement.getString(4);
            return result;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            closeDataBaseConnection();
        }
        return null;
    }


	@Override
	public String getTenantRealmAndStatus(FedIdRequestBean response)
	{
		String tenantRealm="";

		try {
			logger.debug(" with response:"+response);
			if(DatabaseConfig.getDataConnection() == null){
				logger.error("Database connection not found");
			}
			CallableStatement callableStatement = databaseConnection.prepareCall("{call FBI_GET_STATUS_TENANT_REALM(?,?,?,?,?)}");
			callableStatement.setString(1, response.getIamUserId());
			callableStatement.setString(2, response.getEmail());
			//for STATUS
			callableStatement.registerOutParameter(3, Types.VARCHAR);
			//for tenant realm
			callableStatement.registerOutParameter(4, Types.VARCHAR);
			//for error message from the db
			callableStatement.registerOutParameter(5, Types.VARCHAR);
			callableStatement.executeUpdate();
			response.setStatus(callableStatement.getString(3));
			tenantRealm = callableStatement.getString(4);
			String message = callableStatement.getString(5);
			closeDataBaseConnection();
			if(!message.contains("SUCCESS")) {
				logger.error(message);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeDataBaseConnection();
			logger.error(e.getMessage());
		}

		return tenantRealm;
	}


    private void closeDataBaseConnection(){
		if(databaseConnection != null){
			try{
				databaseConnection.close();
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}
