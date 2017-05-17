package com.tmobile.federation.services;

import java.io.IOException;
import java.util.StringTokenizer;

import com.tmobile.federation.utils.FederationAppConfig;

import java.util.Base64;




public class AuthenticationService {

	
	public boolean authenticate(String authCredentials) {

		if (null == authCredentials)
			return false;
		// header value format will be "Basic encodedstring" for Basic
		// authentication. Example "Basic YWRtaW46YWRtaW4="
		final String encodedUserPassword = authCredentials.replaceFirst("Basic"
				+ " ", "");
		String usernameAndPassword = null;
		try {
			byte[] decodedBytes = Base64.getDecoder().decode(
					encodedUserPassword);
			usernameAndPassword = new String(decodedBytes, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		final StringTokenizer tokenizer = new StringTokenizer(
				usernameAndPassword, ":");
		final String username = tokenizer.nextToken();
		final String password = tokenizer.nextToken();

	
		boolean authenticationStatus = username.equals(FederationAppConfig.getUserName())
				&& password.equals(FederationAppConfig.getPassword());
		
		return authenticationStatus;
	}

}
