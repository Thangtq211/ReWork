package com.tmobile.federation.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.tmobile.federation.beans.req.userProfile.UserProfiles;
import com.tmobile.federation.beans.res.GlobalErrorBean;
import com.tmobile.federation.beans.res.GlobalResponseBean;
import com.tmobile.federation.utils.RequestBuilder;
import com.tmobile.iam.fedetaion.services.req.beans.Header;
import com.tmobile.mauth.util.AppConfig;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 5/14/2017.
 */
public class RetrieveUserProfile {

    private static final Logger logger = Logger.getLogger(RetrieveUserProfile.class);

    public static GlobalResponseBean retrieveUserProfile(String caller, String tuid, UserProfiles userProfile)  {
        logger.debug(caller);
        ObjectMapper om = new ObjectMapper();
        Object resObj = null;
        try {
            List<Header> headers = new ArrayList<Header>();
            Gson gson = new Gson();
            String jo = gson.toJson(userProfile);
            logger.debug("Json request: " + jo);
            final BasicCookieStore cookieStore = new BasicCookieStore();
            HttpResponse<String> response = RequestBuilder.send(cookieStore,
              "iamTimeout",
              "GET",
              AppConfig.getProperty("wsgHostPort")+"/ucc/documentManagement/v1/userProfile/users/"+ tuid,
              headers,
              null,
              jo.toString(),
              "authenticateb -> wsg -> getUserProfile -> " + caller);

            String output = null;
            if(response != null) {
                output = response.getBody();
            }


            logger.debug("Output from Server .... \n");
            logger.debug(output);



            GlobalResponseBean grb= new GlobalResponseBean();
            GlobalErrorBean err = new GlobalErrorBean();
            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                grb.setStatusCode("-3000");
                err.setErrorDescription(response.getStatus()+ "Error in calling WSGService");
                err.setErrorDetail(StringEscapeUtils.escapeJson(output));
                grb.setObject(err);
                return grb;
            }
            grb.setStatusCode("0");
            resObj =  om.readValue(output, Object.class);
            logger.debug(resObj.toString());
        } catch (Exception ex) {
            logger.error("Could not parse the output", ex);
            return new GlobalResponseBean("-3001" , ex.getMessage());
        }

        return new GlobalResponseBean("0" , "Success");
    }
}
