package com.nimai.ucm.utility;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nimai.ucm.bean.BusinessDetailsBean;
import com.nimai.ucm.bean.OwnerMasterBean;
import com.nimai.ucm.bean.PersonalDetailsBean;
import com.nimai.ucm.service.PersonalDetailsService;
import com.nimai.ucm.service.RegisterUserService;

@Component
public class ValidateUserDetails {

	@Autowired
	RegisterUserService registerUser;

	@Autowired
	PersonalDetailsService personalDetail;

	public String validatePersonalDetails(PersonalDetailsBean personDetailsBean) {

		final Pattern COUNTRY_NAME = Pattern.compile("^[a-zA-Z]*$");
		//final Pattern EMAIADDRESS = Pattern.compile("^(.+)@(.+)$");
		final Pattern FIRSTNAME = Pattern.compile("^([a-zA-Z '-]+)$");
		final Pattern LASTNAME = Pattern.compile("^([a-zA-Z '-]+)$");
		final Pattern MOBILENUM = Pattern.compile("^[0-9]*$");
		final Pattern SUBSCRIBERTYPE = Pattern.compile("^[a-zA-Z]*$");
		// final Pattern LANDLINENUMBER = Pattern.compile("^[0-9]*$");
		
		final Pattern EMAILPATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
		

		String returnStr = "";
		try {
			if (personDetailsBean != null) {

				boolean isEmailExist = registerUser.checkEmailId(personDetailsBean.getEmailAddress());

				if (isEmailExist) {
					return "Email Id already exists. Please try another email Address.";
				}
				if ((personDetailsBean.getSubscriberType() == null)
						|| (personDetailsBean.getSubscriberType().trim().isEmpty())) {
					return "Please select Subscriber Type";
				}
				if (!SUBSCRIBERTYPE.matcher(personDetailsBean.getSubscriberType()).matches()) {
					return "Invalid Subcriber Type";
				}
				if (!((personDetailsBean.getSubscriberType().equalsIgnoreCase("CUSTOMER"))
						|| (personDetailsBean.getSubscriberType().equalsIgnoreCase("BANK"))
						|| (personDetailsBean.getSubscriberType().equalsIgnoreCase("REFERRER")))) {
					return "Invalid Subscriber Type, Please provide proper subscriber type.";
				}
				if ((personDetailsBean.getSubscriberType().equalsIgnoreCase("BANK"))) {
					if ((personDetailsBean.getBankType() == null) || (personDetailsBean.getBankType().trim().isEmpty())) {
						return "Invalid Bank Type, Please provide valid Bank type.";
					}
					if (!((personDetailsBean.getBankType().equalsIgnoreCase("Customer")) || (personDetailsBean.getBankType().equalsIgnoreCase("Underwriter")))) {
						return "Bank type be either serve as Customer or Underwriter.";
					}
				}

				if ((personDetailsBean.getFirstName() == null) || (personDetailsBean.getFirstName().trim().isEmpty())) {
					return "Please enter first name";
				}
				if (!FIRSTNAME.matcher(personDetailsBean.getFirstName()).matches()) {
					return "Space not allowed in Firstname";
				}
//				if ((personDetailsBean.getFirstName().length() <= 2)
//						|| (personDetailsBean.getFirstName().length() > 25)) {
//					return "First Name should be greater then 3 characters and less then 25 characters";
//				}
				if ((personDetailsBean.getLastName() == null) || (personDetailsBean.getLastName().trim().isEmpty())) {
					return "Please enter last name";
				}
				if (!LASTNAME.matcher(personDetailsBean.getLastName()).matches()) {
					return "Space not allowed in Lastname";
				}
//				if ((personDetailsBean.getLastName().length() <= 2)
//						|| (personDetailsBean.getLastName().length() > 25)) {
//					return "Last Name should be greater then 3 characters and less then 25 characters";
//				}
				if ((personDetailsBean.getEmailAddress() == null)
						|| (personDetailsBean.getEmailAddress().trim().isEmpty())) {
					return "Please enter email address";
				}
				if (!EMAILPATTERN.matcher(personDetailsBean.getEmailAddress()).matches()) {
					return "Please enter valid email address";
				}
//				if (((personDetailsBean.getMobileNum() == null) || (personDetailsBean.getMobileNum().trim().isEmpty()))
//						&& ((personDetailsBean.getLandLinenumber() == null)
//								|| (personDetailsBean.getMobileNum().trim().isEmpty()))) {
//					return "Either mobile number or landline number is required.";
//				}
				
				if((personDetailsBean.getSubscriberType().equalsIgnoreCase("BANK") && ((personDetailsBean.getBankType().equalsIgnoreCase("Underwriter")) || (personDetailsBean.getBankType().equalsIgnoreCase("Customer")))))
				{
					if (((personDetailsBean.getLandLinenumber() == null) || (personDetailsBean.getMobileNum().trim().isEmpty()))) {
						return "Landline number is required.";
					}
				}
				
				if (!(personDetailsBean.getMobileNum() == null) || (personDetailsBean.getMobileNum().trim().isEmpty())) {
					if (!MOBILENUM.matcher(personDetailsBean.getMobileNum()).matches()) {
						return "Mobile number should be digits only";
					}
					if ((personDetailsBean.getMobileNum().length() < 7)) {
						return "Mobile number should be greater than 7 digits";
					}
				}
				if (!((personDetailsBean.getLandLinenumber() == null) || (personDetailsBean.getLandLinenumber().trim().isEmpty()))) {
					if (!MOBILENUM.matcher(personDetailsBean.getLandLinenumber()).matches()) {
						return "Landline Number should be digits only";
					}
					if ((personDetailsBean.getLandLinenumber().length() < 7)) {
						return "Landline Number must be greater than 7 digits";
					}
				}
				if ((personDetailsBean.getCountryName() == null)
						|| (personDetailsBean.getCountryName().trim().isEmpty())) {
					return "Please enter country name";
				}
				if (!COUNTRY_NAME.matcher(personDetailsBean.getCountryName()).matches()) {
					return "Country name always only characters";
				}
				if ((personDetailsBean.getCountryName().length() <= 2) || (personDetailsBean.getCountryName().length() > 25)) {
					return "Country Name should be greater then 3 characters and less then 25 characters";
				}
				if ((personDetailsBean.getSubscriberType().equalsIgnoreCase("REFERRER"))
						&& ((personDetailsBean.getCompanyName() == null)
								|| (personDetailsBean.getCompanyName().trim().isEmpty()))) {
					return "Company Name can't be left empty.";
				}
				if ((personDetailsBean.getSubscriberType().equalsIgnoreCase("REFERRER"))
						&& (personDetailsBean.getDesignation() == null
								|| (personDetailsBean.getDesignation().trim().isEmpty()))) {
					return "Designation can't be left empty.";
				}
				if ((personDetailsBean.getSubscriberType().equalsIgnoreCase("REFERRER"))
						&& (personDetailsBean.getBusinessType() == null
								|| (personDetailsBean.getBusinessType().trim().isEmpty()))) {
					return "BusinessType can't be left empty.";
				}
//				if ((personDetailsBean.getSubscriberType().equalsIgnoreCase("BANK")
//						&& personDetailsBean.getBankType().equalsIgnoreCase("Underwriter"))
//						&& ((personDetailsBean.getMinLCValue() == null)
//								|| (personDetailsBean.getMinLCValue().trim().isEmpty()))) {
//					return "Min LC Value can't be left empty.";
//				}
				if ((personDetailsBean.getSubscriberType().equalsIgnoreCase("BANK")
						&& personDetailsBean.getBankType().equalsIgnoreCase("Underwriter"))
						&& (personDetailsBean.getInterestedCountry().length == 0)) {
					return "Intrested Country can't be left empty.";
				}
				if ((personDetailsBean.getSubscriberType().equalsIgnoreCase("BANK")
						&& personDetailsBean.getBankType().equalsIgnoreCase("Underwriter"))
						&& (personDetailsBean.getBlacklistedGoods().length == 0)) {
					return "Blacklisted goods can't be left empty.";
				}
				returnStr = "Success";
			} else {
				returnStr = "Failure";
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnStr = "Failure : " + e.getMessage();
		}
		return returnStr;
	}

	public String validateBusinessDetails(BusinessDetailsBean businessDetailsBean) {

		boolean isUserIdExist = registerUser.checkUserId(businessDetailsBean.getUserId());

		final Pattern COMPANYNAME = Pattern.compile("^[a-zA-Z@.]*$");
		final Pattern COUNTRY = Pattern.compile("^[a-zA-Z]*$");
		final Pattern DESIGNATION = Pattern.compile("^[a-zA-Z]*$");
		final Pattern OFFCADDRESS = Pattern.compile("\"([^\\\\\"]|\\\\\")*\"");
		final Pattern OFFCNUMBER = Pattern.compile("^[0-9]*$");
		final Pattern OWNERNAME = Pattern.compile("^[a-zA-Z]*$");
		final Pattern REGISTRATIONTYPE = Pattern.compile("^[a-zA-Z]*$");
		final Pattern TELEPHONE = Pattern.compile("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$");
		final Pattern Alphanumeric = Pattern.compile("^[a-zA-Z0-9@]*$");
		final Pattern AlphanumericWithSpecialChar = Pattern.compile("^[-@.\\/#&+\\w\\s]*$");

		String returnStr = "";
		try {
			if (!isUserIdExist) {
				return "UserId is not exist";
			}
			if (businessDetailsBean.getUserId().substring(0, 2).equalsIgnoreCase(
					"BA") ) 
			{
				
				if ((businessDetailsBean.getBankName()) == null || (businessDetailsBean.getBankName().trim().isEmpty())) {
						return "Please enter Bank Name";
					}
				if (!AlphanumericWithSpecialChar.matcher(businessDetailsBean.getBankName()).matches()) {
					return "Please enter alphanumeric character for Bank Name";
				}
				if ((businessDetailsBean.getBranchName()) == null || (businessDetailsBean.getBranchName().trim().isEmpty())) {
						return "Please enter Branch Name";
					}

				if (!AlphanumericWithSpecialChar.matcher(businessDetailsBean.getBranchName()).matches()) {
					return "Please enter alphanumeric character";
				}
				if ((businessDetailsBean.getSwiftCode() == null || (businessDetailsBean.getSwiftCode().trim().isEmpty()))) {
						return "Please enter  Swift Code";
					}
				if (!Alphanumeric.matcher(businessDetailsBean.getSwiftCode()).matches()) {
					return "Please enter alphanumeric character";
				}

			}	
			
			if (businessDetailsBean.getUserId().substring(0, 2).equalsIgnoreCase("CU")  || businessDetailsBean.getUserId().substring(0, 2).equalsIgnoreCase("BC") )
			{
				if ((businessDetailsBean.getRegistrationType()) == null || (businessDetailsBean.getRegistrationType().trim().isEmpty())) {
					return "Please select Business type";
				}
				
				if(businessDetailsBean.getComapanyName().trim().isEmpty())
				{
					return "Company Name is required";
				}
				if ((businessDetailsBean.getComapanyName().length() <= 3) || (businessDetailsBean.getComapanyName().length() >= 45)) {
					return "Company Name should be greater then 3 characters and less then 45 characters";
				}
				if (!AlphanumericWithSpecialChar.matcher(businessDetailsBean.getComapanyName()).matches()) {
					return "Invalid Company Name";
				}
				if ((businessDetailsBean.getOwnerMasterBean().length > 0)) {
					boolean flag = false;
					for (OwnerMasterBean owb : businessDetailsBean.getOwnerMasterBean()) {
						if(owb.getOwnerFirstName() == null || owb.getOwnerLastName() == null || owb.getOwnerFirstName().trim().isEmpty() || owb.getOwnerLastName().trim().isEmpty())
						{
							return "Onwer fields are left empty.";
						}
						if (owb.getOwnerFirstName().length() < 1  || owb.getOwnerFirstName().length() >= 25) {
							flag = true;
							return "Owner Name must be greater than a character and less then 25 characters";

						}
						if (owb.getOwnerLastName().length() < 1 || owb.getOwnerLastName().length() >= 25) {
							flag = true;
							return "Owner lastName must be greater than a character and less then 25 characters";

						}
						if (!OWNERNAME.matcher(owb.getOwnerFirstName()).matches() && !OWNERNAME.matcher(owb.getOwnerLastName()).matches()) {
							flag = true;
							return "Please enter alphabet only";
						}
						if (flag) {
							break;
						}
					}

				}
				
			}
			
//			if ((businessDetailsBean.getDesignation()) == null || (businessDetailsBean.getDesignation().trim().isEmpty())) {
//				return "Please enter Designation";
//			} 
			
			if ((businessDetailsBean.getTelephone()) == null || (businessDetailsBean.getTelephone().trim().isEmpty())) {
				return "Please enter the telephone number";
			}
		
			if ((businessDetailsBean.getRegisteredCountry()) == null || (businessDetailsBean.getRegisteredCountry().trim().isEmpty())) {
				return "Please select company's registered country name";
			}
			if ((businessDetailsBean.getAddress1()) == null || (businessDetailsBean.getAddress1().trim().isEmpty())) {
				return "Please enter Address1 field";
			}
			if ((businessDetailsBean.getAddress2()) == null || (businessDetailsBean.getAddress2().trim().isEmpty())) {
				return "Please enter Address2 field";
			}
//			if ((businessDetailsBean.getAddress3()) == null || (businessDetailsBean.getAddress3().trim().isEmpty())) {
//				return "Please enter address properly";
//			}
			if ((businessDetailsBean.getCity()) == null || (businessDetailsBean.getCity().trim().isEmpty())) {
				return "Please enter city";
			}
			if ((businessDetailsBean.getPincode()) == null || (businessDetailsBean.getPincode().trim().isEmpty())) {
				return "Please enter pincode";
			}	
			if(!Alphanumeric.matcher(businessDetailsBean.getPincode()).matches())
			{
				return "Pincode should be alphanumeric";
				}
			if ((businessDetailsBean.getPincode().length() < 4)) {
				return "Pincode must be greater than 4 digits";
			}
				

			returnStr = "Success";
			}catch(

	Exception e)
	{
			e.printStackTrace();
			returnStr = "Failed : " + e.getMessage();
		}return returnStr;
}

}
