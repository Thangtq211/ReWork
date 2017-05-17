package com.tmobile.federation.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;
 
public class FederationAppConfig {
	
	private final static Logger logger = Logger.getLogger(FederationAppConfig.class.getCanonicalName());
    private static PropertiesConfiguration config = null;
    
    static {
        try {
        	logger.debug("****************Loading AuthenticateB Configuration*****************");
       	    
        	FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
        		    new FileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
        		    	.configure(new Parameters().properties()
        		        .setFileName("authenticateb.properties")
        		        .setThrowExceptionOnMissing(true)
        		        .setReloadingRefreshDelay((long)(10*60*1000))
        		        .setListDelimiterHandler(new DefaultListDelimiterHandler(','))
        		        .setIncludesAllowed(false));
        	
        	config = builder.getConfiguration();

        } catch (ConfigurationException e) {
            logger.error("*************Unable to load AuthenticateB Properties file**************", e);
        } catch (Exception any) {
        	any.printStackTrace();
        	logger.error(any.getMessage());
        }
        
    }
    
    public static synchronized String getProperty(final String key) {
    	StringBuilder strBuilder = new StringBuilder();
    	    	
    	//logger.debug("propoerty reading " + key);
    	if(config != null) {
    		Object o = config.getProperty(key);
    		if(o instanceof String) {
    			String s = (String) o;
    			strBuilder.append(s);
    		} else if(o instanceof ArrayList<?>) {
    			ArrayList<Object> a = (ArrayList<Object>)o;
    			for(Object as : a) {
    				String s = (String) as;
    				if(strBuilder.length() > 0) {
    					strBuilder.append(",");
    					strBuilder.append(s);
    				} else {
    					strBuilder.append(s);
    				}
    			}
    		}
    	}
    	
    	return strBuilder.toString();
    }
    
    public static synchronized List<String> getPropertyAsList(final String key) {
    	List<String> strList = new ArrayList<String>();
    	   
    	//logger.debug("propoerty reading " + key);
    	if(config != null) {
    		Object o = config.getProperty(key);
    		if(o instanceof String) {
    			String s = (String) o;
    			if(s != null)
    				strList.add(s);
    		} else if(o instanceof ArrayList<?>) {
    			ArrayList<Object> a = (ArrayList<Object>)o;
    			for(Object as : a) {
    				String s = (String) as;
    				if(s != null)
    					strList.add(s);
    			}
    		}
    	}
    	
    	return strList;
    }
    
    public static int getMaxDomainLength() {
    	return Integer.parseInt(config.getString("DomainDefaultLen","20"));
    }
    
    public static String getDefaultMaxUsers(){
    	return config.getString("DefaultMaxUsers","20");
    }
    
    public static String getCreatedBy() {
    	return config.getString("CreatedBy","authenticateb");
    }
    public static String getLogoURLType() {
    	return config.getString("LogoUrlType","LogoURL");
    }
    
    public static String getDbUrl() {
    	return config.getString("DbUrl","DbUrl");
    }
    
    public static String getDbUser() {
    	return config.getString("DbUser","jhipster");
    }
    
    public static String getDbPwd() {
    	return config.getString("DbPwd","DbPwd");
    }

	public static String getUserName() {
			return config.getString("Username","userNAme");
	}

	public static String getPassword() {

		return config.getString("password","password");
	}
    
    

}