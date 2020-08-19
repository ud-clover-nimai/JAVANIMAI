package com.nimai.email.service;

import java.util.ArrayList;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nimai.email.bean.EmailContentBean;

@Component
public class EmailServiceProviderImpl {

	@Autowired    
	EmailContentBean emailContentBean;
	
	
	public void sendemail(ArrayList  input) throws MessagingException
	 {
		System.out.println("in EmailServiceProviderImpl");

		 emailContentBean.setFromEmailAddress((String)input.get(1));
		 emailContentBean.setToEmailAddress((ArrayList)input.get(2));
		 emailContentBean.setCcEmailAddress((ArrayList)input.get(3));
		 emailContentBean.setBccEmailAddress((ArrayList)input.get(4));
		 emailContentBean.setSubject((String)input.get(5));
		 emailContentBean.setEmailBody((String)input.get(6));
		 
		 
		 emailContentBean.setListOfAttachements((ArrayList)input.get(9));
	   
		 System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		 System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		 
		emailContentBean.sendEmail();
		 
	 }
}
