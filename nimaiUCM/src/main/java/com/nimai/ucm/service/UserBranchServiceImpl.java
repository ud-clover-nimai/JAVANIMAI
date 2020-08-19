package com.nimai.ucm.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimai.ucm.bean.UserBranchBean;
import com.nimai.ucm.entity.Customer;
import com.nimai.ucm.entity.UserBranchEntity;
import com.nimai.ucm.repository.CustomerRepository;
import com.nimai.ucm.repository.UserBranchRepository;

@Service
public class UserBranchServiceImpl implements UserBranchService {

	//Changes from Sravan
	private static final Logger LOGGER = LoggerFactory.getLogger(UserBranchServiceImpl.class);
	//End 
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private UserBranchRepository userBranchRepository;

	@Override
	public String saveUserBranchDetails(UserBranchBean branchUserBean, String userid) {
		Customer customer =	customerRepository.findByUSERID(userid);
		//Changes from Sravan
		LOGGER.info("Save User Branch Details method is invoked in UserBranchServiceIMpl class");
		//LOGGER.info(" Batch Id "+branchUserBean.getBatch_id()+" Email Id "+branchUserBean.getEmail_id()+" Employee Id "+branchUserBean.getEmployee_id()+" Employee Name "+branchUserBean.getEmployee_name()+" Id "+branchUserBean.getId()+" Insert By "+branchUserBean.getInsert_by()+" Modify By "+branchUserBean.getModify_by()+" Passcode Value "+branchUserBean.getPasscode_value()+" User Id "+branchUserBean.getUser_id());
		//End
		String email = customer.getEmail_address();
		String customerdomain = email .substring(email .indexOf("@") + 1);
		String credentialemail = branchUserBean.getEmail_id();
		String validateemail = credentialemail .substring(credentialemail .indexOf("@") + 1);
		if(customerdomain.equals(validateemail)) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserBranchEntity userBranchEntity = modelMapper.map(branchUserBean, UserBranchEntity.class);
		userBranchRepository.save(userBranchEntity);
		String responseID  = Integer.toString(userBranchEntity.getId());
		
		return responseID;
		}
		else {
			return "Registered Email Domain not matched";
		}
	}
}
