package com.nimai.ucm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimai.ucm.bean.GenericResponse;
import com.nimai.ucm.bean.UserBranchBean;
import com.nimai.ucm.service.UserBranchService;
import com.nimai.ucm.utility.ErrorDescription;

@RestController
@RequestMapping("/UserBranch")
public class UserBranchController {
	//Changes By Sravan
	static Logger LOGGER = LoggerFactory.getLogger(UserBranchController.class.getName());
	
	@Autowired
	private UserBranchService userBranchService;
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping(value = "/userBranch/{userid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveUserBranch(@RequestBody UserBranchBean branchUserBean, @PathVariable("userid") String userid){
		GenericResponse<Object> response = new GenericResponse<Object>();
		//Changes By Sravan
		LOGGER.info("=====================UserBranchController=====================");
		LOGGER.info("saveUserBranch method is invoked");
		LOGGER.info(" Batch Id "+branchUserBean.getBatch_id()+" Email Id "+branchUserBean.getEmail_id());
		try {
			//Changes By Sravan
			System.out.println("Inside the controller "+userid+" branchDetails "+branchUserBean.getEmployee_name()+" "+branchUserBean.getEmail_id()+" employee id "+branchUserBean.getEmployee_id());
			branchUserBean.setUser_id(userid);
			response.setData(userBranchService.saveUserBranchDetails(branchUserBean, userid));
			
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

	}
}
