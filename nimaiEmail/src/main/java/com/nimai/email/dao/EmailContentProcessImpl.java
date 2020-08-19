package com.nimai.email.dao;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nimai.email.entity.EmailComponentMaster;
import com.nimai.email.utility.FileAttachment;
import com.nimai.email.utility.InlineImage;

@Component
public class EmailContentProcessImpl {

	@Autowired
	EmailConfigurationdaoImpl emailConfigurationDAOImpl;

//@Override
	/*
	 * **************************************************************** PARAMETER
	 * STRUTURE TO BE FOLLOWED for ARRAYLIST to be passed in METHOD process
	 * ****************************************************************** input
	 * ArrayList 0 - eventId 1 - to email addresss 2 - Arraylist of parameters for
	 * subject 3 - Arraylist of parameters for body 4 - Arraylist of files for
	 * attachement
	 * ******************************************************************
	 */
	/*
	 * **************************************************************** PARAMETER
	 * STRUTURE TO BE FOLLOWED for ARRAYLIST to be returned by METHOD process
	 * ****************************************************************** input
	 * ArrayList 0 - eventId 1 - from email address 2 - Arraylist of TO email
	 * addresses 3 - Arraylist of CC email addresses 4 - Arraylist of BCC email
	 * addresses 5 - String of Subject 6 - String of Body 7 - ArrayList of File
	 * attachements 8 - ArrayList of Inline Images
	 * ******************************************************************
	 */
	public ArrayList process(ArrayList input) {

		ArrayList output = null;
		if (input != null) {
			Integer eventId = (Integer) input.get(0);
			String toEmailId = (String) input.get(1);

			System.out.println("@#####eventId= " + eventId);
			System.out.println("@#####input.get(0)= " + input.get(0));
			System.out.println("@#####input.get(1)= " + input.get(1));
			System.out.println("@#####input.get(2)= " + input.get(2));
			System.out.println("@#####input.get(3)= " + input.get(3));
			// System.out.println("@#####input.get(4)= "+input.get(4));
//                  System.out.println("@#####input.get(5)= "+input.get(5));
//                  System.out.println("@#####input.get(6)= "+input.get(6));

			// EmailCompMapHandler hEmailComponent=EmailCompMapHandler.getInstance();
			// EmailCompMap emailCompMap=hEmailComponent.get(eventId);
			EmailComponentMaster emailConfigurationBean = null;
			// EmailConfigurationDAO emailConfigurationDAO=new EmailConfigurationDAOImpl();

			// EmailConfigurationDAOImpl emailDaoimpl= new EmailConfigurationDAOImpl();

			emailConfigurationBean = (EmailComponentMaster) emailConfigurationDAOImpl.findByID(eventId.longValue());
			// emailConfigurationBean= (FxEmailcomponentmaster) emailInsert.findByID(100L);

			/* hhh* HibernateUtil.commitTransaction();/* */

			// System.out.println(emailCompMap.getEmailFrom());
			// System.out.println(emailCompMap.getEventId());
			/// System.out.println(getSubject(emailCompMap,input));
			// System.out.println(getBody(emailCompMap,input));
			// System.out.println(input.get(1));
			output = new ArrayList();
			output.add(eventId);
			output.add(emailConfigurationBean.getEmailFrom());

			if (toEmailId != null && toEmailId.trim().length() != 0) {
				ArrayList<String> toList = new ArrayList(Arrays.asList(toEmailId.split("\\s*,\\s*")));
				System.out.println(">>>>>>>>>>>>>" + toList.size());
				output.add(toList);
			} else {
				output.add(null);
			}

			if (emailConfigurationBean.getCc() != null && emailConfigurationBean.getCc().trim().length() != 0) {
				ArrayList<String> ccList = new ArrayList(
						Arrays.asList(emailConfigurationBean.getCc().split("\\s*,\\s*")));
				output.add(ccList);
			} else {
				output.add(null);
			}

			if (emailConfigurationBean.getBcc() != null && emailConfigurationBean.getBcc().trim().length() != 0) {
				ArrayList<String> bccList = new ArrayList(
						Arrays.asList(emailConfigurationBean.getBcc().split("\\s*,\\s*")));
				output.add(bccList);
			} else {
				output.add(null);
			}

			output.add(getSubject(emailConfigurationBean, input));

			output.add(getBody(emailConfigurationBean, input));

			// output.add(input.get(2));
			// output.add(input.get());

//		output.add(getInlineImages(emailConfigurationBean));
//                
//                String javaCodeImage ="0:image2:ruppee_symbol.JPG";
//                
//                javaCodeImage = "" + javaCodeImage +","+ emailConfigurationBean.getInlineimage();
//                output.add(javaCodeImage);
//                //output.add(emailConfigurationBean.getBodyencrypt());
//                output.add(getTotalAttachementList(emailConfigurationBean,(ArrayList)input.get(4)));

			/*
			 * try{ System.out.println("insideeeeeeee:input.size()="+input.size());
			 * if(input.size() >=5){ System.out.println("===="+input.get(4));
			 * output.add(input.get(4));
			 * 
			 * } }catch(Exception e){ e.printStackTrace(System.out); }
			 */
		}

		return output;
	}

	private ArrayList<File> getTotalAttachementList(EmailComponentMaster emailConfigurationBean,
			ArrayList originalFileList) {
		ArrayList<File> finalFileList = null;

		if (originalFileList != null && originalFileList.size() != 0) {
			finalFileList = new ArrayList<File>();
			finalFileList.addAll(originalFileList);
		}

		String fileListFromDB = emailConfigurationBean.getAttachment();
		if (fileListFromDB != null && fileListFromDB.trim().length() != 0) {

			if (finalFileList == null) {
				finalFileList = new ArrayList<File>();
			}

			String arrayFiles[] = fileListFromDB.split(",");
			for (String filedetails : arrayFiles) {
				String details[] = filedetails.split(":");
				FileAttachment fileAttachment = new FileAttachment();
				fileAttachment.setFileType(details[0]);
				fileAttachment.setFileName(details[1]);
				fileAttachment.process(emailConfigurationBean);

				finalFileList.add(fileAttachment.getFileObject());
			}

		}

		return finalFileList;
	}

	private ArrayList<InlineImage> getInlineImages(EmailComponentMaster emailConfigurationBean) {
		ArrayList<InlineImage> listOfInlineImages = null;

		String inlineImages = emailConfigurationBean.getInlineimage();
		if (inlineImages != null && inlineImages.trim().length() > 0) {
			System.out.println("inlineImages=" + inlineImages);
			listOfInlineImages = new ArrayList<InlineImage>();
			String array[] = inlineImages.split(",");
			for (String inlineImageDetails : array) {
				String imageDetails[] = inlineImageDetails.split(":");
				InlineImage inlineImage = new InlineImage();
				inlineImage.setFileType(imageDetails[0]);
				inlineImage.setTagName(imageDetails[1]);
				inlineImage.setFileName(imageDetails[2]);
//			inlineImage.process(emailConfigurationBean);
				inlineImage.process(emailConfigurationBean);
				listOfInlineImages.add(inlineImage);
			}

		} else {
			listOfInlineImages = null;
		}

		return listOfInlineImages;
	}

	private String getSubject(EmailComponentMaster emailConfigurationBean, ArrayList input) {

		String subjectTemplate = emailConfigurationBean.getSubject();
		System.out.println("@@@" + input.get(2));
		// HashMap hmSubject=(HashMap)input.get(2);
		HashMap<String, String> hmSubjectn = new HashMap<>();
		hmSubjectn.put(subjectTemplate, subjectTemplate);

		// return replaceAll(hmSubject,subjectTemplate);
		return replaceAll(hmSubjectn, subjectTemplate);
	}

	private String getBody(EmailComponentMaster emailConfigurationBean, ArrayList input) {
		String bodyTemplate = emailConfigurationBean.getBody();
		HashMap hmBody = (HashMap) input.get(3);

		return replaceAll(hmBody, bodyTemplate);
	}

	private String replaceAll(HashMap hm, String str) {

		System.out.println("--------------------------------------");
		System.out.println(str);
		System.out.println("--------------------------------------");
		Set<String> keys = hm.keySet();
		Iterator<String> itr = keys.iterator();
		while (itr.hasNext()) {
			String key = itr.next();
			String value = (String) hm.get(key);
			if (key.equalsIgnoreCase("username") && str.contains("${username}")) {
				str = str.replace("${" + key + "}", value);
			} else if (key.equalsIgnoreCase("userId") && str.contains("${userId}")) {
				str = str.replace("${" + key + "}", value);
			} else if (key.equalsIgnoreCase("link") && str.contains("${link}")) {
				str = str.replace("${" + key + "}", value);
			} else if (key.equalsIgnoreCase("passcode") && str.contains("${passcode}")) {
				str = str.replace("${" + key + "}", value);
			} else if (key.equalsIgnoreCase("transactionId") && str.contains("${transactionId}")) {
				str = str.replace("${" + key + "}", value);
			} else if (key.equalsIgnoreCase("quotationId") && str.contains("${quotationId}")) {
				str = str.replace("${" + key + "}", value);
			} else if (key.equalsIgnoreCase("reason") && str.contains("${reason}")) {
				str = str.replace("${" + key + "}", value);
			} 
			else if (key.equalsIgnoreCase("username") && str.contains("${username}")) {
				str = str.replace("${" + key + "}", value);
			}else if (key.equalsIgnoreCase("lcIssuingValue") && str.contains("${lcIssuingValue}")) {
				str = str.replace("${" + key + "}", value);
			}else if (key.equalsIgnoreCase("lcIssuingDate") && str.contains("${lcIssuingDate}")) {
				str = str.replace("${" + key + "}", value);
			}else if (key.equalsIgnoreCase("lcExpiryDate") && str.contains("${lcExpiryDate}")) {
				str = str.replace("${" + key + "}", value);
			}
			else if (key.equalsIgnoreCase("customerName") && str.contains("${customerName}")) {
				str = str.replace("${" + key + "}", value);
			}
			else if (key.equalsIgnoreCase("totalQuoteValue") && str.contains("${totalQuoteValue}")) {
				str = str.replace("${" + key + "}", value);
			}
			else if (key.equalsIgnoreCase("validatyDate") && str.contains("${validatyDate}")) {
				str = str.replace("${" + key + "}", value);
			}
			else
				str = str.replaceAll("\\$" + key + "\\$", value);
		}

		System.out.println("body=" + str);

		return str;
	}

	private static String replaceAll1(HashMap hm, String str) {

		Set<String> keys = hm.keySet();
		Iterator<String> itr = keys.iterator();
		while (itr.hasNext()) {
			String key = itr.next();
			String value = (String) hm.get(key);
			str = str.replaceAll("\\$" + key + "\\$", value);
		}
		System.out.println("body=" + str);

		return str;
	}

}
