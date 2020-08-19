package com.nimai.uam.controller;

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

import com.nimai.uam.bean.ChangePasswordBean;
import com.nimai.uam.bean.GenericResponse;
import com.nimai.uam.bean.LoginRequest;
import com.nimai.uam.bean.ResetPasswordBean;
import com.nimai.uam.entity.NimaiMLogin;
import com.nimai.uam.service.UserService;
import com.nimai.uam.utility.ErrorDescription;
import com.nimai.uam.utility.ResetUserValidation;

@RestController
@RequestMapping("passwordPolicy")
public class passwordPolicyController {

	@Autowired
	UserService resetUserService;

	@Autowired
	ResetUserValidation resetUserValidation;

	public passwordPolicyController() {
		System.out.println("password policy load");
	}

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "signIn", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Object> userSignIn(@RequestBody LoginRequest loginRequestBean) {
		GenericResponse<Object> response = new GenericResponse<Object>();
		String errorString = this.resetUserValidation.loginRequestValidation(loginRequestBean);
		boolean checkUserDetails = false;

		if (errorString.equalsIgnoreCase("success")) {
			try {
				boolean nimaiLogin = resetUserService.checkSignIncrendentials(loginRequestBean);
				System.out.println(nimaiLogin);
				if (nimaiLogin == false) {
					response.setErrCode("ASA009");
					response.setMessage(ErrorDescription.getDescription("ASA009"));
					return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
				}
				response.setFlag(1);
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} catch (Exception e) {

				response.setErrCode("EXE000");
				response.setMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}
		} else {
			response.setErrCode("EXE000");
			response.setMessage(ErrorDescription.getDescription("EXE000") + errorString.toString());
		}
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping(value = "/resetPassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> resetpasswordFirstAttempt(@RequestBody ResetPasswordBean resetPasswordBean)

	{
		GenericResponse<Object> response = new GenericResponse<Object>();
		String errorString = this.resetUserValidation.passwordValidation(resetPasswordBean);

		if (errorString.equalsIgnoreCase("success")) {
			try {
				NimaiMLogin nimaiLogin = resetUserService.saveResetPasswordDetails(resetPasswordBean);

				response.setErrCode("ASA001");
				response.setMessage(ErrorDescription.getDescription("ASA001"));
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} catch (Exception e) {
				response.setErrCode("EXE000");
				response.setMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}
		} else {
			response.setErrCode("EXE000");
			response.setMessage(ErrorDescription.getDescription("EXE000") + errorString.toString());
		}
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping(value = "/changePassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordBean changePasswordBean) {

		GenericResponse<Object> response = new GenericResponse<Object>();
		
		String errorString = this.resetUserValidation.changePasswordValidation(changePasswordBean);

		if (errorString.equalsIgnoreCase("success")) {
			try {
				NimaiMLogin nimaiLogin = resetUserService.saveChangedPasswordDetails(changePasswordBean);
				response.setMessage("success");
				response.setData(changePasswordBean.getUserId());
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} catch (Exception e) {
				response.setErrCode("EXE000");
				response.setMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}
		} else {
			response.setErrCode("EXE000");
			response.setMessage(ErrorDescription.getDescription("EXE000") + errorString.toString());
		}
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping(value = "/usertoken/{token}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> findUserByToken(@PathVariable("token") String token){
		GenericResponse<String> response = new GenericResponse<>();
		try {
			NimaiMLogin loginUser = this.resetUserService.getUserDetailsByTokenKey(token);
			if(loginUser != null) {
				response.setData(String.valueOf(loginUser.getUserid()));
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}else {
				response.setData("Invalid User");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
			
			
		}catch(Exception e) {
			return new ResponseEntity<Object>(null,HttpStatus.BAD_REQUEST);
		}
	}

}
