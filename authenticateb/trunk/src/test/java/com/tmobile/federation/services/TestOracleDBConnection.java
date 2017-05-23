package com.tmobile.federation.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.*;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by THANGTQ on 5/19/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestOracleDBConnection.class })
public class TestOracleDBConnection {

    @Test
    public void getOracleDataConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = null;
            DriverManager.setLoginTimeout(10);
            connection = DriverManager.getConnection("jdbc:oracle:thin:@10.30.165.150:1521/big_data", "jhipster", "jhip123ster");
            assertThat(connection, is(notNullValue()));
            boolean isValidConnection = false;
            if (connection != null) {
                Statement statement = null;
                ResultSet rs = null;
                try {
                    statement = connection.createStatement();
                    rs = statement.executeQuery("SELECT SYSDATE FROM DUAL");
                    while (rs.next()) isValidConnection = true;
                    assertThat(isValidConnection, is(true));
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

        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
