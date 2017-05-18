package com.tmobile.federation.dao;

import com.tmobile.federation.beans.req.FedIdRequestBean;
import com.tmobile.federation.config.DatabaseConfig;
import org.springframework.stereotype.Service;

import java.sql.*;

/**
 *
 * @author Admin
 *
 *
 * @since 1.0
 */
@Service
public class IAMUserDAOImpl implements IAMUserDAO {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(IAMUserDAOImpl.class);
    private Connection databaseConnection;

    public IAMUserDAOImpl() {
        this.databaseConnection = DatabaseConfig.getDataConnection();
    }

    @Override
    public String createFederatedId(String email, String userId, String tenantID, String createdBy) {
        CallableStatement callableStatement = null;
        try {
            if (DatabaseConfig.getDataConnection() == null) {
                logger.error("Database connection not found");
            }
            checkAndReTryConnection();
            callableStatement = databaseConnection.prepareCall("{call fbi_stg_tenant_user_chk(?,?,?,?,?)}");
            callableStatement.setString(1, userId);
            callableStatement.setString(2, tenantID);
            callableStatement.setString(3, email);
            callableStatement.setString(4, createdBy);
            callableStatement.registerOutParameter(5, Types.VARCHAR);
            callableStatement.executeUpdate();
            String result = callableStatement.getString(5);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (callableStatement != null) callableStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        CallableStatement callableStatement = null;
        try {
            logger.debug("getting FederatedId with iamUserId:" + iamUserId);
            if (DatabaseConfig.getDataConnection() == null) {
                logger.error("Database connection not found");
            }
            checkAndReTryConnection();
            callableStatement = databaseConnection.prepareCall("{call fbi_stg_user_fed_id(?,?,?,?)}");
            callableStatement.setString(1, iamUserId);
            callableStatement.setString(2, email);
            callableStatement.setString(3, tenantId);
            callableStatement.registerOutParameter(4, Types.VARCHAR);
            callableStatement.executeUpdate();
            String result = callableStatement.getString(4);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (callableStatement != null) callableStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String getTenantRealmAndStatus(FedIdRequestBean response) {
        String tenantRealm = "";
        CallableStatement callableStatement = null;

        try {
            logger.debug(" with response:" + response);
            if (DatabaseConfig.getDataConnection() == null) {
                logger.error("Database connection not found");
            }
            checkAndReTryConnection();
            callableStatement = databaseConnection.prepareCall("{call FBI_GET_STATUS_TENANT_REALM(?,?,?,?,?)}");
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
            if (!message.contains("SUCCESS")) {
                logger.error(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            try {
                if (callableStatement != null) callableStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return tenantRealm;
    }



    private void checkAndReTryConnection() {
        boolean isValidConnection = false;
        if (databaseConnection != null) {
            Statement statement = null;
            ResultSet rs = null;
            try {
                statement = databaseConnection.createStatement();
                rs = statement.executeQuery("SELECT SYSDATE FROM DUAL");
                while (rs.next()) isValidConnection = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    rs.close();
                    statement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if(!isValidConnection)
            this.databaseConnection = DatabaseConfig.getDataConnection();
    }

}
