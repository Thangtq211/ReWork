package com.tmobile.federation.utils;

import java.util.Base64;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class Utils {
	
	public static String encrypt(String name, String password) {
		String authString = name + ":" + password;
		System.out.println("auth string: " + authString);
		byte[] authEncBytes = Base64.getEncoder().encode(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		System.out.println("Base64 encoded auth string: " + authStringEnc);
		return authStringEnc;
	}
	
	
	public static String generateRandomString(int length) {
		StringBuilder randomString = new StringBuilder(length);
		for (int i=0; i< length;i++) {
			randomString.append(generateRandomCharacter("abcdefghijklmnopqrstuvwxzy"));
		}
		return randomString.toString();
	}
	
	public static String generateRandomCharacter(String characters) {
		int len=characters.length();
		int r=(int)(len*Math.random());
		return characters.substring(r,r+1);
	}
	public static boolean isValidURL(String url) {  

	    URL u = null;

	    try {  
	        u = new URL(url);  
	    } catch (MalformedURLException e) {  
	        return false;  
	    }

	    try {  
	        u.toURI();  
	    } catch (URISyntaxException e) {  
	        return false;  
	    }  

	    return true;  
	} 
	
	public static void main(String[] args ) {
		System.out.println(Utils.isValidURL("http://xyz.com"));
		System.out.println(Utils.isValidURL("http://xyz.com/abc.jpg"));
		System.out.println(Utils.encrypt("userNAme","password"));
	}	
	
	
}
