package com.nimai.ucm.controller;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nimai.ucm.bean.BranchUserBean;
import com.nimai.ucm.bean.BusinessDetailsBean;
import com.nimai.ucm.bean.GenericResponse;
import com.nimai.ucm.bean.PersonalDetailsBean;
import com.nimai.ucm.service.CountryService;
import com.nimai.ucm.service.RegisterUserService;
import com.nimai.ucm.utility.ErrorDescription;
import com.nimai.ucm.utility.RegistrationId;
import com.nimai.ucm.utility.ValidateUserDetails;

@RestController
@RequestMapping("/UserDetails")
public class UserDetailController {

	static Logger logger = Logger.getLogger(UserDetailController.class.getName());

	@Autowired
	ValidateUserDetails vud;

	@Autowired
	RegisterUserService registerUser;

	@Autowired
	RegistrationId register;

	@Autowired
	CountryService countryService;

	/*----------------- Method For Personal Details ---------------------*/
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping(value = "/registerUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> registerUser(@RequestBody PersonalDetailsBean personDetailsBean) {
		GenericResponse<Object> response = new GenericResponse<Object>();
		logger.info(" ================ UserDetail Controller ================");
		System.out.println(personDetailsBean.toString());
		String errorString = this.vud.validatePersonalDetails(personDetailsBean);
		if (errorString.equalsIgnoreCase("Success")) {
			try {
				personDetailsBean = registerUser.savePersonalDetails(personDetailsBean);
				response.setStatus(errorString);
				response.setData(personDetailsBean);
				response.setErrCode("ASA001");
				response.setErrMessage(ErrorDescription.getDescription("ASA001"));
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} catch (Exception e) {
				response.setStatus("Failure");
				response.setErrCode("EXE000");
				response.setErrMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}

		} else {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(errorString);
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

	}

	/*---------------------- Method For View Personal Detail -----------------------*/
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/viewPersonalDetails/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Object> viewPersonalDetails(@PathVariable String userId) {
		GenericResponse<Object> response = new GenericResponse<Object>();
		try {
			PersonalDetailsBean personDetailsBean = registerUser.getPersonalDetails(userId);

			response.setData(personDetailsBean);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} catch (Exception exception) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			logger.error(exception.getMessage());

			if (exception.getMessage().contains("Unable to find"))
				response.setErrMessage("Records not Found");
			else
				response.setErrMessage(ErrorDescription.getDescription("EXE001"));
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}

	/*---------------------- Method For update Personal Detail -----------------------*/
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/updatePersonalDetails", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Object> updatePersonalDetails(@RequestBody PersonalDetailsBean pdb) {
		GenericResponse<Object> response = new GenericResponse<Object>();
		try {
			PersonalDetailsBean personDetailsBean = registerUser.updatePersonalDetails(pdb);
			response.setData(personDetailsBean);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} catch (Exception exception) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + exception.getMessage());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}

	/*---------------------- Method For Update Business Detail -----------------------*/
	@CrossOrigin("*")
	@PostMapping(value = "/updateBusinessDetails", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateBusinessDetail(@RequestBody BusinessDetailsBean businessDetailsBean) {
		System.out.println(businessDetailsBean);
		GenericResponse<Object> response = new GenericResponse<Object>();

		logger.info(" ============== UserDetail : Business Details ============== ");
		System.out.println(businessDetailsBean);

		if (businessDetailsBean.getUserId().toString().substring(0, 2).equalsIgnoreCase("RE")) {
			response.setErrCode("ASA005");
			response.setErrMessage(ErrorDescription.getDescription("ASA005"));
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

		String errorString = this.vud.validateBusinessDetails(businessDetailsBean);

		logger.info("\n ======== UserDetail : Business Details Validation Status ========== " + errorString);

		if (errorString.equalsIgnoreCase("success")) {
			try {
				boolean status = registerUser.saveBusinessDetails(businessDetailsBean.getUserId(), businessDetailsBean);
				response.setErrCode("ASA002");
				response.setErrMessage(ErrorDescription.getDescription("ASA002"));
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} catch (Exception e) {
				response.setErrCode("EXE000");
				response.setErrMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}
		} else {
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + errorString.toString());
		}
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	/*---------------------- Method For View Business Detail -----------------------*/
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/viewBusinessDetails/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Object> viewBusinessDetails(@PathVariable String userId) {
		GenericResponse<Object> response = new GenericResponse<Object>();
		try {

			BusinessDetailsBean businessDetailsBean = registerUser.getBusinessDetails(userId);
			response.setData(businessDetailsBean);
			return new ResponseEntity<Object>(response, HttpStatus.OK);

		} catch (Exception exception) {
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + exception.getMessage());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}

	/*---------------------- check Email ID Exists -----------------------*/
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/checkEmailID", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Object> checkEmailID(@RequestBody PersonalDetailsBean personalDetailsBean) {
		GenericResponse<Object> response = new GenericResponse<Object>();
		try {
			boolean status = registerUser.checkEmailId(personalDetailsBean.getEmailAddress());
			if (status != true) {
				response.setStatus("Success");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} else {
				response.setStatus("Failure");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
		} catch (Exception exception) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + exception.getMessage());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

	}

	/*---------------------- Is Email ID Exists -----------------------*/
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/isEmailExists/{emailAddress}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Object> isEmailExists(@PathVariable String emailAddress) {
		GenericResponse<Object> response = new GenericResponse<Object>();
		try {
			boolean status = registerUser.checkEmailId(emailAddress);
			if (status == true) {
				response.setStatus("Success");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} else {
				response.setStatus("Failure");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
		} catch (Exception exception) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + exception.getMessage());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}

	/*---------------------- Branch User Details -----------------------*/
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/saveBranchUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Object> generatePassword(@RequestBody BranchUserBean branchUserbean) {
		GenericResponse<Object> response = new GenericResponse<Object>();

		registerUser.saveBranchUser(branchUserbean);

		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/viewDetailedCountry", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Object> viewDetailedCountries() {
		GenericResponse<Object> response = new GenericResponse<Object>();
		logger.info(" ================ Drop Down : Countries with States================");

		try {
			List<?> country = countryService.countryData();
			response.setData(country);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} catch (Exception exception) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			logger.error(exception.getMessage());

			if (exception.getMessage().contains("Unable to find"))
				response.setErrMessage("Records not Found");
			else
				response.setErrMessage(ErrorDescription.getDescription("EXE001"));
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
