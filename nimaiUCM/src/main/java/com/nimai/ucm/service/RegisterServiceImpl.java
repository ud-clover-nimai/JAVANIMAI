package com.nimai.ucm.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimai.ucm.bean.BlackListedGoodsBean;
import com.nimai.ucm.bean.BranchUserBean;
import com.nimai.ucm.bean.BusinessDetailsBean;
import com.nimai.ucm.bean.InterestedCountryBean;
import com.nimai.ucm.bean.OwnerMasterBean;
import com.nimai.ucm.bean.PersonalDetailsBean;
import com.nimai.ucm.entity.BlackListedGoods;
import com.nimai.ucm.entity.BranchUser;
import com.nimai.ucm.entity.InterestedCountry;
import com.nimai.ucm.entity.NimaiCustomer;
import com.nimai.ucm.entity.NimaiMLogin;
import com.nimai.ucm.entity.OwnerMaster;
import com.nimai.ucm.repository.BlackListedGoodsRepository;
import com.nimai.ucm.repository.InterestedCountryRepository;
import com.nimai.ucm.repository.LoginRepository;
import com.nimai.ucm.repository.OwnerMasterRepository;
import com.nimai.ucm.repository.UserDetailRepository;
import com.nimai.ucm.utility.PasswordGenerator;
import com.nimai.ucm.utility.RegistrationId;

@Service
@Transactional
public class RegisterServiceImpl implements RegisterUserService {

	//Chaanges from Sravan
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterServiceImpl.class);
	
	@Autowired
	RegistrationId userid;

	@Autowired
	UserDetailRepository detailRepository;

	@Autowired
	LoginRepository loginRepository;

	@Autowired
	PasswordGenerator password;

	@Autowired
	InterestedCountryRepository icr;

	@Autowired
	BlackListedGoodsRepository blgr;

	@Autowired
	OwnerMasterRepository omr;

	@Override
	public PersonalDetailsBean savePersonalDetails(PersonalDetailsBean personDetailsBean) {
		//Changes from Sravan
		LOGGER.info("savePersonalDetails methods is invoked in RegisterServiceImpl class");
		LOGGER.info(" Bank Type "+personDetailsBean.getBankType()+" Business Type "+personDetailsBean.getBusinessType()+" Company Name "+personDetailsBean.getCompanyName()+" Country Name "+personDetailsBean.getCountryName()+" Designation "+personDetailsBean.getDesignation()+" Emai Address "+personDetailsBean.getEmailAddress()+" First Name "+personDetailsBean.getFirstName()+" Land Line Number"+personDetailsBean.getLandLinenumber()+" Last Name "+personDetailsBean.getLastName()+" Min LCValue "+personDetailsBean.getMinLCValue()+" Mobile Num "+personDetailsBean.getMobileNum()+" Subscriber Type "+personDetailsBean.getSubscriberType()+" User Id "+personDetailsBean.getUserId());
		//End
		NimaiCustomer nc = new NimaiCustomer();

		String subscriberType = personDetailsBean.getSubscriberType();
		String bankType = "";
		String userID = "";
		if (personDetailsBean.getBankType() == null)
			bankType = "";
		else
			bankType = personDetailsBean.getBankType().toUpperCase();

		System.out.println(subscriberType + "   " + bankType);
		userID = userid.username(subscriberType, bankType);
		personDetailsBean.setUserId(userID);

		nc.setUserid(personDetailsBean.getUserId());
		nc.setSubscriberType(personDetailsBean.getSubscriberType().toUpperCase());

		nc.setFirstName(personDetailsBean.getFirstName());
		nc.setLastName(personDetailsBean.getLastName());
		nc.setEmailAddress(personDetailsBean.getEmailAddress());
		nc.setMobileNumber(personDetailsBean.getMobileNum());
		nc.setLandline(personDetailsBean.getLandLinenumber());
		nc.setCountryName(personDetailsBean.getCountryName());

		// Changes By Shubham Patil
		nc.setIsRegister(true);
		nc.setAccountType(personDetailsBean.getAccount_type());
		nc.setAccountSource(personDetailsBean.getAccount_source());
		nc.setAccountStatus(personDetailsBean.getAccount_status());
		nc.setAccountCreatedDate(personDetailsBean.getAccount_created_date());
		nc.setRegCurrency(personDetailsBean.getRegCurrency());
		nc.setEmailAddress1(personDetailsBean.getEmailAddress1());
		nc.setEmailAddress2(personDetailsBean.getEmailAddress2());
		nc.setEmailAddress3(personDetailsBean.getEmailAddress3());
		// End

		if (personDetailsBean.getSubscriberType().equalsIgnoreCase("BANK")) {
			nc.setBankType(personDetailsBean.getBankType().toUpperCase());
			nc.setBusinessType(personDetailsBean.getBusinessType());
			nc.setMinValueofLc(personDetailsBean.getMinLCValue());
			nc.setRegCurrency(personDetailsBean.getRegCurrency());

		} else {
			nc.setBusinessType("");
			nc.setMinValueofLc("");
			nc.setBankType("");
		}
		if (personDetailsBean.getSubscriberType().equalsIgnoreCase("REFERRER")) {
			nc.setCompanyName(personDetailsBean.getCompanyName());
			nc.setDesignation(personDetailsBean.getDesignation());
			nc.setBusinessType(personDetailsBean.getBusinessType());
		} else {
			nc.setCompanyName("");
			nc.setDesignation("");
			nc.setBusinessType("");
		}

		nc.setInsertedDate(Calendar.getInstance().getTime());
		nc.setModifiedDate(Calendar.getInstance().getTime());

		NimaiCustomer customerRegister = detailRepository.save(nc);

		if (personDetailsBean.getSubscriberType().equalsIgnoreCase("BANK")) {

			for (BlackListedGoodsBean blgBean : personDetailsBean.getBlacklistedGoods()) {

				if (blgBean.getGoods_ID() == null) {
					BlackListedGoods blg = new BlackListedGoods();
					blg.setGoodsName(blgBean.getBlackListGoods());
					blg.setInsertedDate(Calendar.getInstance().getTime());
					blg.setUserId(nc);
					blg.setGoodsMId(blgBean.getGoodsMId());
					saveBlackListedGoods(blg);
				} else {
					updateBlackListedGoods(blgBean);
				}

			}

			for (InterestedCountryBean intCon : personDetailsBean.getInterestedCountry()) {
				
				if(intCon.getCountryID() == null) {
					InterestedCountry ic = new InterestedCountry();
					ic.setCountryName(intCon.getCountriesIntrested());
					ic.setInsertedDate(Calendar.getInstance().getTime());
					ic.setUserId(nc);
					ic.setCountryCurrencyId(intCon.getCcid());	
					saveInterestedCountry(ic);
				} else {
					updateInterestedCountry(intCon);
				}

			}

		}

		if (customerRegister != null) {

			NimaiMLogin loginEntity = new NimaiMLogin();

			loginEntity.setUserid(customerRegister);
			loginEntity.setPassword(password.getInitialPassword(8));
			loginEntity.setUserType(personDetailsBean.getSubscriberType().toUpperCase());

			loginEntity.setIsActPassed("INACTIVE");
			loginEntity.setFlag("0");
			loginEntity.setStatus("INACTIVE");

			loginEntity.setInsertedDate(Calendar.getInstance().getTime());
			loginEntity.setModifiedDate(Calendar.getInstance().getTime());

			// loginEntity.setToken(customerRegister.getUserid());
			// loginEntity.setToken_exp_Date(Calendar.getInstance().getTime());

			loginEntity.setLastActivityTime(Calendar.getInstance().getTime());
			loginEntity.setLastLoginTime(Calendar.getInstance().getTime());

			loginRepository.save(loginEntity);
		}

		return personDetailsBean;

	}

	@Override
	public boolean saveBusinessDetails(String userId, BusinessDetailsBean businessDetailsBean) {
		//Changes from Sravan
		LOGGER.info("saveBusinessDetails method is invoked in RegisterServiceImpl class");
		LOGGER.info(" Address1 "+businessDetailsBean.getAddress1()+" Address2 "+businessDetailsBean.getAddress2()+" Address3 "+businessDetailsBean.getAddress3()+" Bank Name "+businessDetailsBean.getBankName()+" Branch Name "+businessDetailsBean.getBranchName()+" City "+businessDetailsBean.getCity()+" Company Name "+businessDetailsBean.getComapanyName()+" Designation "+businessDetailsBean.getDesignation()+" Pin Code "+businessDetailsBean.getPincode()+" Province Name "+businessDetailsBean.getProvinceName()+" Registered Country "+businessDetailsBean.getRegisteredCountry()+" Registration Type "+businessDetailsBean.getRegistrationType()+" Swift Code "+businessDetailsBean.getSwiftCode()+" Telephone "+businessDetailsBean.getTelephone()+" User Id "+businessDetailsBean.getUserId());
		NimaiCustomer nc = detailRepository.getOne(businessDetailsBean.getUserId());

		if (nc != null) {
			nc.setBankNbfcName(businessDetailsBean.getBankName());
			nc.setAddress3(businessDetailsBean.getAddress3());
			nc.setAddress1(businessDetailsBean.getAddress1());
			nc.setAddress2(businessDetailsBean.getAddress2());
			nc.setBranchName(businessDetailsBean.getBranchName());
			nc.setCity(businessDetailsBean.getCity());
			nc.setProvincename(businessDetailsBean.getProvinceName());
			nc.setPincode(businessDetailsBean.getPincode());
			nc.setRegistrationType(businessDetailsBean.getRegistrationType());
			nc.setSwiftCode(businessDetailsBean.getSwiftCode());
			nc.setRegistredCountry(businessDetailsBean.getRegisteredCountry());
			nc.setInsertedDate(Calendar.getInstance().getTime());
			nc.setModifiedDate(Calendar.getInstance().getTime());
			nc.setCompanyName(businessDetailsBean.getComapanyName());
			nc.setTelephone(businessDetailsBean.getTelephone());
			nc.setDesignation(businessDetailsBean.getDesignation());
			// Changes by shubham patil
			nc.setIsBDetailsFilled(true);
			// End

			for (OwnerMasterBean ombean : businessDetailsBean.getOwnerMasterBean()) {

				if (ombean.getOwnerID() == null) {
					OwnerMaster om = new OwnerMaster();
					om.setOwnerFirstName(ombean.getOwnerFirstName());
					om.setOwnerLastName(ombean.getOwnerLastName());
					om.setDesignation(ombean.getDesignation());
					om.setInsertedDate(Calendar.getInstance().getTime());
					om.setModifiedDate(Calendar.getInstance().getTime());

					om.setUserId(nc);
					saveOwnerMaster(om);
				} else {
					updateOwnerMaster(ombean);
				}

			}

			NimaiCustomer client = detailRepository.save(nc);
			return true;
		}
		return false;

	}


	@Override
	public PersonalDetailsBean getPersonalDetails(String userId) {

		NimaiCustomer nc = detailRepository.getOne(userId);

		PersonalDetailsBean pdb = new PersonalDetailsBean();

		pdb.setUserId(nc.getUserid());
		pdb.setSubscriberType(nc.getSubscriberType());
		pdb.setBankType(nc.getBankType());
		pdb.setFirstName(nc.getFirstName());
		pdb.setLastName(nc.getLastName());
		pdb.setEmailAddress(nc.getEmailAddress());
		pdb.setMobileNum(nc.getMobileNumber());
		pdb.setLandLinenumber(nc.getLandline());
		pdb.setCountryName(nc.getCountryName());
		pdb.setDesignation(nc.getDesignation());
		pdb.setCompanyName(nc.getCompanyName());
		pdb.setBusinessType(nc.getBusinessType());
		pdb.setMinLCValue(nc.getMinValueofLc());
		pdb.setIsRegister(nc.getIsRegister());
		pdb.setIsBDetailsFilled(nc.getIsBDetailsFilled());
		pdb.setIsSPlanPurchased(nc.getIsSPlanPurchased());
		pdb.setModeOfPayment(nc.getModeOfPayment());
		pdb.setPaymentDate(nc.getPaymentDate());
		pdb.setKycStatus(nc.getKycStatus());
		pdb.setKycApprovalDate(nc.getKycApprovalDate());
		pdb.setIsRmAssigned(nc.getIsRmAssigned());
		pdb.setRmId(nc.getRmId());
		pdb.setEmailAddress1(nc.getEmailAddress1());
		pdb.setEmailAddress2(nc.getEmailAddress2());
		pdb.setEmailAddress3(nc.getEmailAddress3());
		
		
		List<InterestedCountry> icbr = icr.findByUserId(nc);
		InterestedCountryBean[] ibn = new InterestedCountryBean[icbr.size()];
		int incre = 0;
		for (InterestedCountry ict : icbr) {
			InterestedCountryBean ib = new InterestedCountryBean();
			ib.setCountriesIntrested(ict.getCountryName());
			ib.setCcid(ict.getCountryCurrencyId());
			ib.setCountryID(ict.getCountryID());
			ibn[incre] = ib;
			incre++;
		}

		List<BlackListedGoods> blglist = blgr.findByUserId(nc);
		BlackListedGoodsBean[] blgbean = new BlackListedGoodsBean[blglist.size()];
		int blgCount = 0;
		for (BlackListedGoods blg : blglist) {
			BlackListedGoodsBean blb = new BlackListedGoodsBean();
			blb.setBlackListGoods(blg.getGoodsName());
			blb.setGoods_ID(blg.getGoods_ID());
			blb.setGoodsMId(blg.getGoodsMId());
			blgbean[blgCount] = blb;
			blgCount++;
		}
		pdb.setInterestedCountry((icbr.isEmpty()) ? null : ibn);
		pdb.setBlacklistedGoods((blglist.isEmpty()) ? null : blgbean);

		return pdb;
	}

	@Override
	public BusinessDetailsBean getBusinessDetails(String userId) {

		NimaiCustomer nc = detailRepository.getOne(userId);

		BusinessDetailsBean bdb = new BusinessDetailsBean();

		bdb.setUserId(nc.getUserid());

		bdb.setBankName(nc.getBankNbfcName());
		bdb.setBranchName(nc.getBranchName());
		bdb.setSwiftCode(nc.getSwiftCode());
		bdb.setDesignation(nc.getDesignation());
		
		bdb.setComapanyName(nc.getCompanyName());
		bdb.setRegistrationType(nc.getRegistrationType());

		bdb.setRegisteredCountry(nc.getRegistredCountry());
		bdb.setProvinceName(nc.getProvincename());

		bdb.setAddress1(nc.getAddress1());
		bdb.setAddress2(nc.getAddress2());
		bdb.setAddress3(nc.getAddress3());
		bdb.setTelephone(nc.getTelephone());

		bdb.setCity(nc.getCity());
		bdb.setPincode(nc.getPincode());
		bdb.setOwnerMasterBean(new OwnerMasterBean[] {});
		bdb.setIsBDetailsFilled(nc.getIsBDetailsFilled());
		
		
		List<OwnerMaster> owners = omr.findByUserId(nc);

		if (!owners.isEmpty()) {
			OwnerMasterBean[] ob = new OwnerMasterBean[owners.size()];
			for (int x = 0; x < ob.length; x++) {
				OwnerMaster om = owners.get(x);
				OwnerMasterBean obn = new OwnerMasterBean();
				obn.setDesignation(om.getDesignation());
				obn.setOwnerFirstName(om.getOwnerFirstName());
				obn.setOwnerID(om.getOwnerID());
				obn.setOwnerLastName(om.getOwnerLastName());
				ob[x] = obn;

			}
			bdb.setOwnerMasterBean(ob);
		}

		return bdb;
	}

	@Override
	public boolean checkEmailId(String emailId) {
		//Changes from Sravan
		LOGGER.info("checkEmailID method is invoked in RegisterServiceImpl class");
		LOGGER.info("Email Id"+emailId);
		return detailRepository.existsByEmailAddress(emailId);
	}

	@Override
	public boolean checkUserId(String userId) {
		//Changes From Sravan
		LOGGER.info("checkUserID method is invoked in RegisterServiceImpl class");
		LOGGER.info("User Id"+userId);
		return detailRepository.existsById(userId);
	}

	@Override
	public NimaiMLogin saveUserCredentials(NimaiMLogin loginEntity) {
		//Changes From Sravan
		LOGGER.info("SaveUserCredentials method is invoked in RegisterServiceImpl class");
		LOGGER.info("loginEntity "+loginEntity);
		return loginRepository.save(loginEntity);
	}

	@Override
	public NimaiMLogin saveUserCredentials() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PersonalDetailsBean updatePersonalDetails(PersonalDetailsBean pdb) {
		//Changes From Sravan
		LOGGER.info("updatePersonalDetails method is invoked in RegisterServiceImpl class");
		LOGGER.info(" Bank Type "+pdb.getBankType()+" Business Type "+pdb.getBusinessType()+" Company Name "+pdb.getCompanyName()+" Country Name "+pdb.getCountryName()+" Designation "+pdb.getDesignation()+" Email Address "+pdb.getEmailAddress()+" First Name "+pdb.getFirstName()+" Land Line Number "+pdb.getLandLinenumber()+" LastName "+pdb.getLastName()+" MinLCVlue "+pdb.getMinLCValue()+" Mobile Num "+pdb.getMobileNum()+" Subscriber Type "+pdb.getSubscriberType()+" User Id "+pdb.getUserId());
		NimaiCustomer nc = detailRepository.getOne(pdb.getUserId());

		nc.setFirstName(pdb.getFirstName());
		nc.setLastName(pdb.getLastName());
		nc.setEmailAddress(pdb.getEmailAddress());
		nc.setMobileNumber(pdb.getMobileNum());
		nc.setLandline(pdb.getLandLinenumber());
		nc.setCountryName(pdb.getCountryName());

		nc.setCompanyName(pdb.getCompanyName());
		nc.setBusinessType(pdb.getBusinessType());

		nc.setMinValueofLc(pdb.getMinLCValue());
		
		nc.setEmailAddress1(pdb.getEmailAddress1());
		nc.setEmailAddress2(pdb.getEmailAddress2());
		nc.setEmailAddress3(pdb.getEmailAddress3());
		
		// Need to add Countries Interested and Blacklisted Goods

		nc = detailRepository.save(nc);

		pdb.setFirstName(nc.getFirstName());
		pdb.setLastName(nc.getLastName());
		pdb.setEmailAddress(nc.getEmailAddress());
		pdb.setMobileNum(nc.getMobileNumber());
		pdb.setLandLinenumber(nc.getLandline());
		pdb.setCountryName(nc.getCountryName());
		pdb.setCompanyName(nc.getCompanyName());
		pdb.setBusinessType(nc.getBusinessType());
		
		pdb.setEmailAddress1(nc.getEmailAddress1());
		pdb.setEmailAddress2(nc.getEmailAddress2());
		pdb.setEmailAddress3(nc.getEmailAddress3());

		// Need to add Countries Interested and Blacklisted Goods
		pdb.setSubscriberType(nc.getSubscriberType());

		if (nc.getSubscriberType().equalsIgnoreCase("BANK") && nc.getBankType().equalsIgnoreCase("Underwriter")) {

			for (BlackListedGoodsBean blgBean : pdb.getBlacklistedGoods()) {
				if (blgBean.getGoods_ID() == null) {

					BlackListedGoods blg = new BlackListedGoods();
					blg.setGoodsName(blgBean.getBlackListGoods());
					blg.setInsertedDate(Calendar.getInstance().getTime());
					blg.setUserId(nc);
					blg.setGoodsMId(blgBean.getGoodsMId());
					saveBlackListedGoods(blg);
				}

			}

			for (InterestedCountryBean intCon : pdb.getInterestedCountry()) {
				if (intCon.getCountryID() == null) {
					InterestedCountry ic = new InterestedCountry();
					ic.setCountryName(intCon.getCountriesIntrested());
					ic.setInsertedDate(Calendar.getInstance().getTime());
					ic.setUserId(nc);
					ic.setCountryCurrencyId(intCon.getCcid());
					saveInterestedCountry(ic);
				}
			}
			pdb.setMinLCValue(nc.getMinValueofLc());

		}

		return pdb;
	}

	@Override
	public void saveInterestedCountry(InterestedCountry ic) {
		//Changes From Sravan
		LOGGER.info("Save Interested Country method is invoked in RegisterServiceImpl class");
		LOGGER.info(" Country Name "+ic.getCountryName()+" Country Currency Id "+ic.getCountryCurrencyId()+" Country Id "+ic.getCountryID()+" Inserted Date "+ic.getInsertedDate()+" Modified Date "+ic.getModifiedDate()+" User Id "+ic.getUserId());
		icr.save(ic);
	}

	@Override
	public void saveBlackListedGoods(BlackListedGoods blg) {
		//Changes From Sravan
		LOGGER.info("Save Black Listed Goods method is invoked in RegisterServiceImpl class");
		LOGGER.info(" Goods Name "+blg.getGoodsName()+" Goods ID "+blg.getGoods_ID()+" GoodsMId "+blg.getGoodsMId()+" Inserted Date "+blg.getInsertedDate()+" Modified Date "+blg.getModifiedDate()+" User Id "+blg.getUserId());
		blgr.save(blg);
	}

	@Override
	public void updateInterestedCountry(InterestedCountryBean icb) {
		//Changes From Sravan
		LOGGER.info("Update Interested Country method is invoked in RegisterServiceImpl class");
		LOGGER.info(" Ccid "+icb.getCcid()+" Country Id "+icb.getCountryID());
		InterestedCountry ic = icr.getOne(icb.getCountryID());
		if (ic != null) {
			ic.setCountryName(icb.getCountriesIntrested());
			ic.setInsertedDate(Calendar.getInstance().getTime());
			ic.setCountryCurrencyId(icb.getCcid());
			icr.save(ic);
		}

	}

	@Override
	public void updateBlackListedGoods(BlackListedGoodsBean blgb) {
		//Changes From Sravan
		LOGGER.info("Update Black Listed Goods method is invoked in RegisterServiceImpl class");
		LOGGER.info(" Black List Goods "+blgb.getBlackListGoods()+" Goods ID "+blgb.getGoods_ID()+" Goods MId "+blgb.getGoodsMId());
		BlackListedGoods blg = blgr.getOne(blgb.getGoods_ID());
		if (blg != null) {
			blg.setGoodsName(blgb.getBlackListGoods());
			blg.setInsertedDate(Calendar.getInstance().getTime());
			blg.setGoodsMId(blgb.getGoodsMId());
			blgr.save(blg);
		}

	}

	@Override
	public void saveOwnerMaster(OwnerMaster om) {
		//Changes From Sravan
		LOGGER.info("saveOwnerMaster method is invoked in RegisterServiceimpl Class");
		LOGGER.info(" Designation "+om.getDesignation()+" Owner First Name "+om.getOwnerFirstName()+" Owner Last Name "+om.getOwnerLastName()+" Owner ID "+om.getOwnerID()+" Inserted Date "+om.getInsertedDate()+" Modified Date "+om.getModifiedDate()+" User Id "+om.getUserId());
		omr.save(om);
	}

	@Override
	public void updateOwnerMaster(OwnerMasterBean om) {
		//Changes From Sravan
		LOGGER.info("saveOwnerMaster method is invoked in RegisterServiceimpl Class");
		LOGGER.info(" Designation "+om.getDesignation()+" Owner First Name "+om.getOwnerFirstName()+" Owner Last Name "+om.getOwnerLastName()+" Owner ID "+om.getOwnerID());
		OwnerMaster omm = omr.getOne(om.getOwnerID());
		if (omm != null) {
			omm.setDesignation(om.getDesignation());
			omm.setModifiedDate(new Date());
			omm.setOwnerFirstName(om.getOwnerFirstName());
			omm.setOwnerLastName(om.getOwnerLastName());
			omr.save(omm);
		}

	}

	@Override
	public BusinessDetailsBean updateBusinessDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BranchUserBean saveBranchUser(BranchUserBean branchUserbean) {
		//Changes From Sravan
		LOGGER.info("Save Branch User method is invoked");
		LOGGER.info(" Branch Id "+branchUserbean.getBranchId()+" Email Id "+branchUserbean.getEmailId()+" Employee Id "+branchUserbean.getEmployeeId()+" Inserted By "+branchUserbean.getInsertedBy()+" Inserted Date "+branchUserbean.getInsertedDate()+" Modified By "+branchUserbean.getModifiedBy()+" Modified Date "+branchUserbean.getModifiedDate()+" Passcode Value "+branchUserbean.getPasscodeValue()+" Token Id "+branchUserbean.getTokenId()+" User Id "+branchUserbean.getUserID());
		BranchUser nimaipwd= new BranchUser();
		nimaipwd.setBranchId(branchUserbean.getBranchId());
		nimaipwd.setEmailId(branchUserbean.getEmailId());
		nimaipwd.setEmployeeId(branchUserbean.getEmployeeId());

		nimaipwd.setInsertedBy(branchUserbean.getInsertedBy());
		nimaipwd.setInsertedDate(branchUserbean.getInsertedDate());
		nimaipwd.setModifiedBy(branchUserbean.getModifiedBy());
		nimaipwd.setModifiedDate(branchUserbean.getModifiedDate());
//		
//		NimaiUniquePwd nimaipwd1=new NimaiUniquePwd();
//		nimaipwd.setTokenId(nimaipwd1.uniqueNumber());
//	
//		nimaipedrepo.save(nimaipwd);
		return branchUserbean;
	}
}
