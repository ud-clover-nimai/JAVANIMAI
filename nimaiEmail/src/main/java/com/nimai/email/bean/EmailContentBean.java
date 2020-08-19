package com.nimai.email.bean;

import java.io.File;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Component;

import com.nimai.email.constants.ConfigurationValues;
import com.nimai.email.emailproperties.MasterFilePath;
import com.nimai.email.emailproperties.PropertyHandler;
import com.nimai.email.utility.InlineImage;
import com.sun.mail.smtp.SMTPAddressFailedException;

@Component
public class EmailContentBean {

	final static Logger logger = LoggerFactory.getLogger(EmailContentBean.class);

	@Value("${mail.username}")
	private String emailaddress;

	@Value("${mail.password}")
	private String pwd;

	private ConfigurationValues configurationValues;

	@Value("${mail.host}")
	private String host;

	@Value("${mail.smtp.port}")
	private String port;

	@Value("${mail.smtp.starttls.enable}")
	private String tlsEnable;

	@Value("${mail.smtp.auth}")
	private String auth;

	@Value("${mail.smtp.socketFactory.class}")
	private String socketFactoryClass;

	@Value("${mail.smtp.socketFactory.fallback}")
	private String socketFactoryfallback;

	@Value("${mail.smtp.quitwait}")
	private String quitwait;

	@Value("${mail.debug}")
	private String debug;

	@Value("${mail.store.protocol}")
	private String storeProtocol;

	@Value("${mail.transport.protocol}")
	private String transportProtocol;

	private Session mailSession;
	private ArrayList<String> ccEmailAddress;
	private ArrayList<String> bccEmailAddress;
	private String subject;
	private String emailBody;
	private ArrayList<File> listOfAttachements;
	ArrayList<InlineImage> listOfInlineImages;

	@Value("${mail.username}")
	private String fromEmailAddress;

	private ArrayList<String> toEmailAddress;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getEmailaddress() {
		return emailaddress;
	}

	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}

	public ConfigurationValues getConfigurationValues() {
		return configurationValues;
	}

	public void setConfigurationValues(ConfigurationValues configurationValues) {
		this.configurationValues = configurationValues;
	}

	public Session getMailSession() {
		return mailSession;
	}

	public void setMailSession(Session mailSession) {
		this.mailSession = mailSession;
	}

	public ArrayList<String> getCcEmailAddress() {
		return ccEmailAddress;
	}

	public void setCcEmailAddress(ArrayList<String> ccEmailAddress) {
		this.ccEmailAddress = ccEmailAddress;
	}

	public ArrayList<String> getBccEmailAddress() {
		return bccEmailAddress;
	}

	public void setBccEmailAddress(ArrayList<String> bccEmailAddress) {
		this.bccEmailAddress = bccEmailAddress;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public ArrayList<InlineImage> getListOfInlineImages() {
		return listOfInlineImages;
	}

	public void setListOfInlineImages(ArrayList<InlineImage> listOfInlineImages) {
		this.listOfInlineImages = listOfInlineImages;
	}

	public ArrayList<String> getToEmailAddress() {
		return toEmailAddress;
	}

	public void setToEmailAddress(ArrayList<String> toEmailAddress) {
		this.toEmailAddress = toEmailAddress;
	}

	public ArrayList<File> getListOfAttachements() {
		return listOfAttachements;
	}

	public void setListOfAttachements(ArrayList<File> listOfAttachements) {
		this.listOfAttachements = listOfAttachements;
	}

	public String getFromEmailAddress() {
		return fromEmailAddress;
	}

	public void setFromEmailAddress(String fromEmailAddress) {
		this.fromEmailAddress = fromEmailAddress;
	}

	public void sendEmail() throws MessagingException {

		try {
			System.out.println(" ************* EmailContentBean.sendEmail() ************* ");
			// PropertyHandler propertyHandler = new
			// PropertyHandler(MasterFilePath.getMasterfilepath());

			Properties properties = new Properties();

			properties.put("mail.smtp.auth", auth);
			properties.put("mail.smtp.host", host);
			properties.put("mail.smtp.port", port);
			properties.put("mail.transport.protocol", transportProtocol);
			properties.put("mail.smtp.socketFactory.class", socketFactoryClass);

			Authenticator auth = new EmailContentBean.SMTPAuthenticator();
			mailSession = Session.getInstance(properties, auth);
			mailSession.setDebug(true);

			setFromEmailAddress(getFromEmailAddress());
			setMailSession(mailSession);

			MimeMessage msg = new MimeMessage(getMailSession());
			msg.setContent(getEmailBody(), "text/html");
			System.out.println(" ************* EmailContentBean Message Body *************\n " + getEmailBody());

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(getEmailBody(), "text/html");

			MimeMultipart multipart = new MimeMultipart("related");
			multipart.addBodyPart(messageBodyPart);

			// Email Attachment Code Starts here

			ArrayList<File> attachments = getListOfAttachements();
			if (attachments != null && attachments.size() != 0) {

				Iterator<File> itr = attachments.iterator();
				System.out.println("Inside itr method");
				while (itr.hasNext()) {
					System.out.println("=====2");
					File tempFile = itr.next();
					System.out.println(tempFile);
				//	if (tempFile.isFile()) {
						System.out.println("=====3");
						MimeBodyPart fileAttachement = new MimeBodyPart();
						FileDataSource fds = new FileDataSource(tempFile);
						System.out.println("==========++++++======++++++++==========++++++=======__temp file path"+tempFile);
						fileAttachement.setDataHandler(new DataHandler(fds));
						fileAttachement.setFileName(fds.getName());
						multipart.addBodyPart(fileAttachement);
					//}
				}
			}

			//// second part (the image)
			/*
			 * messageBodyPart = new MimeBodyPart(); //DataSource fds = new
			 * FileDataSource("C:\\images\\avivalogo.gif");
			 * 
			 * DataSource fds = new FileDataSource("/AvivaImages/avivalogo.gif");
			 * 
			 * System.out.
			 * println("<<<----change the path as /images/avivalogo.gif------------------>>>>>>"
			 * ); messageBodyPart.setDataHandler(new DataHandler(fds));
			 * messageBodyPart.setHeader("Content-ID","<image>");
			 * 
			 * // add it multipart.addBodyPart(messageBodyPart);
			 */
			// put everything together
			// seting body
			msg.setContent(multipart);

			// seting from email address
			// msg.setFrom(new InternetAddress(getFromEmailAddress()));

			int i = 0;

			// new to

			String toEmails = getToEmailAddress().toString().replaceAll("[+^]", "");
			System.out.println(" ************* To Email *************" + toEmails);

			if (toEmails != null && !toEmails.isEmpty()) {
				toEmails = toEmails.replace("[", "");
				toEmails = toEmails.replace("]", "");
				toEmails = toEmails.replace(" ", "");
				System.out.println("--------" + toEmails + "----------");
				msg.addRecipients(Message.RecipientType.TO, toEmails);
			}

			i = 0;
			// cc
			/*
			 * List<String> ccEmail = getCcEmailAddress(); if (ccEmail != null &&
			 * ccEmail.size() != 0) {
			 * 
			 * InternetAddress[] Internetaddress = new InternetAddress[ccEmail.size()];
			 * Iterator<String> itr = ccEmail.iterator(); while (itr.hasNext()) {
			 * Internetaddress[i] = new InternetAddress(itr.next());
			 * msg.setRecipient(Message.RecipientType.CC, Internetaddress[i++]); }
			 * 
			 * }
			 */

			if (getCcEmailAddress() != null) {

				System.out.println(" ************* CC Details ************* " + getCcEmailAddress().toString());
				String ccEmails = getCcEmailAddress().toString().replaceAll("[+^]", "");
				if (ccEmails != null && !ccEmails.isEmpty()) {
					ccEmails = ccEmails.replace("[", "");
					ccEmails = ccEmails.replace("]", "");
					ccEmails = ccEmails.replace(" ", "");
					System.out.println("--------" + ccEmails + "----------");
					msg.addRecipients(Message.RecipientType.CC, ccEmails);
				}
			} else {
				System.out.println(" ************* CC Details : No CC Email Address Found ************* ");
			}

			i = 0;
			// bcc
			/*
			 * List<String> bccEmail = getBccEmailAddress(); if (bccEmail != null &&
			 * bccEmail.size() != 0) {
			 * 
			 * InternetAddress[] Internetaddress = new InternetAddress[bccEmail.size()];
			 * Iterator<String> itr = bccEmail.iterator(); while (itr.hasNext()) {
			 * Internetaddress[i] = new InternetAddress(itr.next());
			 * msg.setRecipient(Message.RecipientType.BCC, Internetaddress[i++]); }
			 * 
			 * }
			 */

			if (getBccEmailAddress() != null) {
				System.out.println(" ************* BCC Details ************* " + getBccEmailAddress().toString());
				String bccEmails = getBccEmailAddress().toString().replaceAll("[+^]", "");
				if (bccEmails != null && !bccEmails.isEmpty()) {
					bccEmails = bccEmails.replace("[", "");
					bccEmails = bccEmails.replace("]", "");
					bccEmails = bccEmails.replace(" ", "");
					msg.addRecipients(Message.RecipientType.BCC, bccEmails);
				}
			} else {
				System.out.println(" ************* BCC Details : No BCC Email Address Found ************* ");
			}

			msg.setSubject(getSubject());
			msg.setSentDate(new Date());

			if (this.getListOfInlineImages() != null && this.getListOfInlineImages().size() != 0) {

				Iterator<InlineImage> itr = getListOfInlineImages().iterator();
				while (itr.hasNext()) {
					InlineImage inlineImage = itr.next();
					messageBodyPart = new MimeBodyPart();

					DataSource fds = new FileDataSource(inlineImage.getFileObject().getAbsolutePath());
					messageBodyPart.setDataHandler(new DataHandler(fds));
					messageBodyPart.setHeader("Content-ID", "<" + inlineImage.getTagName() + ">");
					messageBodyPart.setHeader("Content-Disposition", "inline");
					messageBodyPart.setDisposition(MimeBodyPart.INLINE);
					// add it
					multipart.addBodyPart(messageBodyPart);
				}

			}

//			int emailenableflag = configurationValues.getIntegerValues("EMAIL", "EMAIL.ENABLE");
//			// int emailenableflag = 1;
//			// Boolean isEmailDisable=new
//			// Boolean(PropertyHandler.getInstance(MasterFilePath.getMasterfilepath()).getProperty("DisableEmailFlag"));
//			if (emailenableflag == 1) {

			Transport.send(msg);

//			} else if (emailenableflag == 1) {
//
//			}

		} catch (MailSendException  e) {
			e.printStackTrace(System.out);
			
			  System.out.println("In Send mail exception Start");
			  
			  // loggingService.saveExceptionLog("sendmail", //
				/*
				 * loggingService.stackTraceToString(me), "Error while sending email to //
				 * customer.", "", "", "", "");
				 */			  
			  // me.printStackTrace(System.out);
			  System.out.println("In Send mail exception End");
		       logger.error("MailSendException found.",e);
		        Exception[] exceptionArray = e.getMessageExceptions();
		        e.getFailedMessages();
		        boolean isSMTPAddressFailedException = false;
		        for(Exception e1 : exceptionArray){
		            if(e1 instanceof SendFailedException){
		                Exception e2 = ((SendFailedException)e1).getNextException();
		                if(e2 instanceof SMTPAddressFailedException){
		                    logger.error("Caught SMTPAddressFailedException. Invalid email id of User/Dealer",e2);
		                  //  utilityService.formatBasicResponseWithMessage(response, ResponseCodes.INVALID_EMAILID, serviceRequestVO.getLanguageId());
		                    isSMTPAddressFailedException=true;
		                    break;
		                }
		            }
			 
		}

	}
	}
	
	private class SMTPAuthenticator extends javax.mail.Authenticator {

		String accontEmailid = emailaddress;
		String accountpwd = pwd;
		// configurationValues.getStringValues("EMAIL", "EMAIL.SEND_EMAIL_ADDRESS");
		// String pwd = configurationValues.getEncryptedValues("EMAIL",
		// "EMAIL.SEND_EMAIL_PWD");
		// String pwd = configurationValues.getStringValues("EMAIL",
		// "EMAIL.SEND_EMAIL_PWD");

		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(emailaddress, pwd); // password not displayed here, but gave the right
																	// password in my actual code.
		}

	}

}
