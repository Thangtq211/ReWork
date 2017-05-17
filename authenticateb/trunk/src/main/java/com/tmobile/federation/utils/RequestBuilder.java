package com.tmobile.federation.utils;

import java.util.List;

import javax.ws.rs.core.Cookie;

import org.apache.http.impl.client.BasicCookieStore;
import org.apache.log4j.Logger;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.tmobile.iam.fedetaion.services.req.beans.Header;
import com.tmobile.mauth.util.ClientHelper;

public class RequestBuilder {
	
	private static final Logger logger = Logger.getLogger(RequestBuilder.class);
	
	public static HttpResponse<String> send(final BasicCookieStore cookieStore, String timeoutProperty, String method, String url, List<Header> headers, Cookie c, String body, String caller) throws Exception {
		
		HttpRequest req = null;
		HttpResponse<String> response = null;
		try {
			logger.debug("Initiating call " + caller);
			Unirest.setHttpClient(ClientHelper.createWithCookieSupport(timeoutProperty, cookieStore));

			switch(method) {
				
				case "GET": {
					req = Unirest.get(url);
					req = req.header("ContentType", "application/json").header("Accept", "application/json").header("Connection", "close");
					if(headers != null && !headers.isEmpty()) {
						for(Header h : headers) {
							req = req.header(h.getName(), h.getValue());
						}
					}
					
					if(c != null) {
						req = req.header("Cookie", c.getName() + "=" + c.getValue());
					}
					
					response = req.asString();
					break;
				}
				case "POST": {
					req = Unirest.post(url);
					req = req.header("ContentType", "application/json").header("Accept", "application/json").header("Connection", "close");
					if(headers != null && !headers.isEmpty()) {
						for(Header h : headers) {
							req = req.header(h.getName(), h.getValue());
						}
					}
					
					if(c != null) {
						req = req.header("Cookie", c.getName() + "=" + c.getValue());
					}
					
					if(body != null) {
						response = ((HttpRequestWithBody)req).body(body).asString();
					} else {
						response = ((HttpRequestWithBody)req).asString();
					}
					break;
				}
				case "PUT": {
					req = Unirest.put(url);
					req = req.header("ContentType", "application/json").header("Accept", "application/json").header("Connection", "close");
					if(headers != null && !headers.isEmpty()) {
						for(Header h : headers) {
							req = req.header(h.getName(), h.getValue());
						}
					}
					
					if(c != null) {
						req = req.header("Cookie", c.getName() + "=" + c.getValue());
					}
					
					if(body != null) {
						response = ((HttpRequestWithBody)req).body(body).asString();
					} else {
						response = ((HttpRequestWithBody)req).asString();
					}
					break;
				}
				case "DELETE": {
					req = Unirest.delete(url);
					req = req.header("ContentType", "application/json").header("Accept", "application/json").header("Connection", "close");
					if(headers != null && !headers.isEmpty()) {
						for(Header h : headers) {
							req = req.header(h.getName(), h.getValue());
						}
					}
					
					if(c != null) {
						req = req.header("Cookie", c.getName() + "=" + c.getValue());
					}
					
					if(body != null) {
						response = ((HttpRequestWithBody)req).body(body).asString();
					} else {
						response = ((HttpRequestWithBody)req).asString();
					}
					break;
				}
				default: {
					throw new Exception("Bad Method Request");
				}
			}
		} catch (Exception e) {
			throw new Exception("Failed " + caller + " " + e.getMessage());
		}
		
		return response;
	}
}