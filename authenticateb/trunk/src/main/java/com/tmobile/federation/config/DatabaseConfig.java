package com.tmobile.federation.config;

import com.tmobile.federation.utils.FederationAppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Admin
 *
 *
 * @since 1.0
 */

public class DatabaseConfig {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DatabaseConfig.class);

    public static Connection getDataConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = null;
            logger.info("DBUrl:"+FederationAppConfig.getDbUrl() + " user:"+FederationAppConfig.getDbUser());
            DriverManager.setLoginTimeout(10);
            connection = DriverManager.getConnection(FederationAppConfig.getDbUrl(), FederationAppConfig.getDbUser(), FederationAppConfig.getDbPwd());


            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
