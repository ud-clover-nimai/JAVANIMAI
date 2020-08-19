package com.nimai.email.utility;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.stereotype.Component;



@Component
public class Utils {
	
	public Date getLinkExpiryDate() {
		Date dNow = new Date();
		Date dafter = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dNow);
		cal.add(Calendar.DATE, 1);
		dafter = cal.getTime();
		System.out.println(dafter);
		return dafter;
	}
	
	public static String generatePasswordResetToken() {
	
				Random objGenerator = new Random(System.currentTimeMillis());
				StringBuilder builder = new StringBuilder();
				int randomNumberLength = 10;
				for (int i = 0; i < randomNumberLength; i++) {
					int digit = objGenerator.nextInt(10);
					builder.append(digit);
				}
				return builder.toString();
			}
	
	public String getEmailDomain(String someEmail)
	{
	    return  someEmail.substring(someEmail.indexOf("@") + 1);
	}
	public static String passcodeValue() {
		
		Random objGenerator = new Random(System.currentTimeMillis());
		StringBuilder builder = new StringBuilder();
		int randomNumberLength = 5;
		for (int i = 0; i < randomNumberLength; i++) {
			int digit = objGenerator.nextInt(10);
			builder.append(digit);
		}
		return builder.toString();
	}
	}

