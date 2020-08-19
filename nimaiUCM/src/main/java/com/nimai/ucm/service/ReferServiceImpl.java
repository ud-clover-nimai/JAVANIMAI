package com.nimai.ucm.service;


import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimai.ucm.bean.ReferBean;
import com.nimai.ucm.entity.Refer;
import com.nimai.ucm.repository.ReferRepository;
import com.nimai.ucm.utility.ReferenceIdUniqueNumber;

@Service
@Transactional
public class ReferServiceImpl implements ReferService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReferServiceImpl.class);
	
	@Autowired
	ReferRepository referRepo;

	@Override
	public void saveReferService(ReferBean referbean,String r1) {
		
	// Chnages From Sravan
	//	Refer refer=new Refer(); 
	//	ReferenceIdUniqueNumber refernceId = new ReferenceIdUniqueNumber();
		/*
		 * refer.setReferenceId(r1);
		 * refer.setBranchUserId(referbean.getBranchUserId());
		 * refer.setCompanyName(referbean.getCompanyName());
		 * refer.setCountryName(referbean.getCountryName());
		 * refer.setEmailAddress(referbean.getEmailAddress());
		 * refer.setFirstName(referbean.getFirstName());
		 * refer.setInsertedBy(referbean.getInsertedBy());
		 * refer.setInsertedDate(referbean.getInsertedDate());
		 * refer.setLastName(referbean.getLastName());
		 * refer.setMobileNo(referbean.getMobileNo());
		 * refer.setModifiedBy(referbean.getModifiedBy());
		 * refer.setModifiedDate(referbean.getModifiedDate());
		 * refer.setStatus(referbean.getStatus());
		 * refer.setUserId(referbean.getUserId());
		 */
		LOGGER.info("SaveReferService method is invoked in ReferServiceImpl class");
		LOGGER.info(" Branch User Id "+referbean.getBranchUserId()+" Company Name "+referbean.getCompanyName()+" Country Name "+referbean.getCountryName()+" Email Address "+referbean.getEmailAddress()+" First Name "+referbean.getFirstName()+" Inserted By "+referbean.getInsertedBy()+" Last Name "+referbean.getLastName()+" Mobile No "+referbean.getMobileNo()+" Modified By "+referbean.getModifiedBy()+" Reference Id "+referbean.getReferenceId()+" Status "+referbean.getStatus()+" User Id "+referbean.getUserId());
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Refer refer = modelMapper.map(referbean, Refer.class);
		ReferenceIdUniqueNumber refernceId = new ReferenceIdUniqueNumber();
		refer.setReferenceId(r1);
		Refer r2=referRepo.save(refer);
		System.out.println(r2.getReferenceId());
			
	}

	@Override
	public List<Refer> getReferByUserId(String userId) {
	    //Changes from Sravan
		LOGGER.info("getReferByUserId method is invoked in ReferServiceImpl class");
		LOGGER.info("User Id "+userId);
     	List<Refer>  refer= referRepo.findReferByUserId(userId);
		return refer;
	}

	@Override
	public boolean checkEmailId(String emailAddress) {
	//Changes from Sravan
		LOGGER.info("checkEmailId method is invoked in ReferServiceImpl class");
		LOGGER.info("Email Address "+emailAddress);
		referRepo.existsByEmailAddress(emailAddress);
		return false;
	}

	@Override
	public Refer updateRefer(ReferBean referbean) {
	//Changes from Sravan
		LOGGER.info("updateRefer method is invoked in ReferServiceImpl class");
		LOGGER.info(" User Id "+referbean.getBranchUserId()+" Company Name "+referbean.getCompanyName()+" Country Name "+referbean.getCountryName()+" Email Address "+referbean.getEmailAddress()+" First Name "+referbean.getFirstName()+" Inserted By "+referbean.getInsertedBy()+" LastName "+referbean.getLastName()+" Mobile No "+referbean.getMobileNo()+" Modified By "+referbean.getModifiedBy()+" Reference Id "+referbean.getReferenceId()+" Status "+referbean.getStatus()+" User Id "+referbean.getUserId());
		
		
		//Changes from Sravan
		/*
		 * Refer refer=new Refer(); referRepo.getOne(referbean.getUserId());
		 * refer.setReferenceId(referbean.getReferenceId());
		 * refer.setBranchUserId(referbean.getBranchUserId());
		 * refer.setCompanyName(referbean.getCompanyName());
		 * refer.setCountryName(referbean.getCountryName());
		 * refer.setEmailAddress(referbean.getEmailAddress());
		 * refer.setFirstName(referbean.getFirstName());
		 * refer.setInsertedBy(referbean.getInsertedBy());
		 * refer.setInsertedDate(referbean.getInsertedDate());
		 * refer.setLastName(referbean.getLastName());
		 * refer.setMobileNo(referbean.getMobileNo());
		 * refer.setModifiedBy(referbean.getModifiedBy());
		 * refer.setModifiedDate(referbean.getModifiedDate());
		 * refer.setStatus(referbean.getStatus());
		 * refer.setUserId(referbean.getUserId());
		 */
		 
		  referRepo.getOne(referbean.getUserId());
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Refer refer = modelMapper.map(referbean, Refer.class);
		
		return	referRepo.save(refer);
		
				
		
				
		
		
	}

	@Override
	public List<Refer> viewReferB(ReferBean referbean) {
		//Changes from Sravan
		 LOGGER.info("viewReferB method is invoked in ReferServiceImpl Class");
		 LOGGER.info(" User Id "+referbean.getBranchUserId()+" Company Name "+referbean.getCompanyName()+" Country Name "+referbean.getCountryName()+" Email Address "+referbean.getEmailAddress()+" First Name "+referbean.getFirstName()+" Inserted By "+referbean.getInsertedBy()+" LastName "+referbean.getLastName()+" Mobile No "+referbean.getMobileNo()+" Modified By "+referbean.getModifiedBy()+" Reference Id "+referbean.getReferenceId()+" Status "+referbean.getStatus()+" User Id "+referbean.getUserId());

		 return referRepo.findAll();
	}

//	public boolean checkEmailId(String emailId) {
//		Refer custEmail=null;
//		try {
//		custEmail=referRepo.isEmailExsists(emailId);
//		if(custEmail!=null && custEmail.getEmailAddress().equals(emailId)) {
//		return true;
//		}else {
//		return false;
//		}
//
//		} catch(Exception e){
//		return false;
//		}
//		}
		

	
}
