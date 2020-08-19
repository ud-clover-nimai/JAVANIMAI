package com.nimai.email.dao;

import java.util.ArrayList;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nimai.email.constants.Constants;
import com.nimai.email.entity.EmailDetails;
import com.nimai.email.service.EmailServiceProviderImpl;
import com.nimai.email.utility.EmailConversionUtil;



@Component
public class EmailProcessImpl {

	private byte actionflag = 100;
	
	@Autowired
	EmailContentProcessImpl emailContentProcessorImpl;

	@Autowired
	EmailServiceProviderImpl emailServiceProviderImpl;

	@Autowired
	EmailDao emailDao;
	

	@Autowired
	EmailConversionUtil emailConversionUtil;
	
	public void sendemail(ArrayList output) throws MessagingException {
		System.out.println("email service provider impl size : " + output.size());
		emailServiceProviderImpl.sendemail(output);

	}
	
	public void sendemail(ArrayList input, byte emailtype) {

		try {
			ArrayList output = emailContentProcessorImpl.process(input);
			String res = output.get(9).toString();
			System.out.println("" + res);
			if (actionflag == Constants.EMAIL_INSERT_INTO_DB) {

				EmailDetails e = emailConversionUtil.createEmailDBObj(output);
				e.setEmailType((int) emailtype);
				// test.print(e);
				emailDao.insertMailDetails(e);

				System.out.println("email service provider impl size : " + output.size());

			} else {
				System.out.println("email service provider impl size : " + output.size());
				emailServiceProviderImpl.sendemail(output);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void saveEmail(ArrayList input, int emailtype) throws Exception {

		// try {
		ArrayList output = emailContentProcessorImpl.process(input);
		// String res = String.class.toString().oin(",",
		// (ArrayList<File>)output.get(9));
		System.out.println(">>>>>><<<<<<" + output.toString());
		String res = output.get(6).toString();
		System.out.println("&&&&&#&&#&#&&#&#&#&&#&#&#&&" + res);

		EmailDetails e = emailConversionUtil.createEmailDBObj(output);
		e.setEmailType(emailtype);
		int x = emailDao.insertMailDetails(e);
		if(x==0) {
		  throw new RuntimeException("Database exception.");
		}
		// System.out.println("email service provider impl size : " + output.size());
//        } catch (Exception e) {
//            e.printStackTrace();
//           
//        }

	}

	/**
	 * @return the actionflag
	 */
	public byte getActionflag() {
		return actionflag;
	}

	/**
	 * @param actionflag the actionflag to set
	 */
	public void setActionflag(byte actionflag) {
		this.actionflag = actionflag;
	}

}
