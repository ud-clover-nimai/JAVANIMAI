package com.nimai.uam.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimai.uam.bean.ChangePasswordBean;
import com.nimai.uam.bean.LoginRequest;
import com.nimai.uam.bean.ResetPasswordBean;
import com.nimai.uam.entity.NimaiMLogin;
import com.nimai.uam.repository.LoginRepository;
import com.nimai.uam.repository.UserDetailRepository;
import com.nimai.uam.utility.MailService;
import com.nimai.uam.utility.Utils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	EntityManager em;

	@Autowired
	LoginRepository loginRepository;

	@Autowired
	UserDetailRepository userDetailsRepository;

	@Autowired
	Utils utils;

	@Autowired
	MailService mail;

	@Override
	public boolean checkUserId(String userId) {
		// TODO Auto-generated method stub
		return userDetailsRepository.existsById(userId);
	}

	@Override
	public boolean checkEmailId(String emailId) {
		// TODO Auto-generated method stub
		return userDetailsRepository.existsByEmailAddress(emailId);
	}

	@Override
	public boolean updateResetToken(ResetPasswordBean resetPasswordBean) {

		Optional<NimaiMLogin> login = loginRepository.findByUserId(resetPasswordBean.getUserId());

		if (login != null) {
			String token = utils.generatePasswordResetToken();
			NimaiMLogin nimaiLogin = login.get();
			Date tokenExpiry = utils.getLinkExpiryDate();
			nimaiLogin.setToken(token);
			nimaiLogin.setTokenExpiryDate(tokenExpiry);
			// nimaiLogin.setResetPasswordStatus('0');
			loginRepository.save(nimaiLogin);

			String link = "http://localhost:4200/forget?key=" + token;
			try {

				mail.sendEmail(link, resetPasswordBean);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return true;
			// }
			// return false;
		}
		return false;

	}

	@Override
	public NimaiMLogin getUserDetailsByTokenKey(String token) {

		NimaiMLogin nimaiLogin = loginRepository.findUserByToken(token);

		return nimaiLogin;

	}

	@Override
	public boolean checkUserTokenKey(String tokenKey) {

		String nimaiLogin = loginRepository.checkUserToken(tokenKey);

		if (nimaiLogin.equals(tokenKey)) {

			return true;
		} else {
			return false;
		}
	}

	@Override
	public NimaiMLogin saveResetPasswordDetails(ResetPasswordBean resetPasswordBean) {
		NimaiMLogin nimaiLoginTOken = loginRepository.findUserByToken(resetPasswordBean.getToken);
		Optional<NimaiMLogin> login = loginRepository.findByUserId(nimaiLoginTOken.getUserid().getUserid());
		NimaiMLogin nimaiLogin = login.get();
		nimaiLogin.setIsActPassed("ACTIVE");
		nimaiLogin.setPassword(resetPasswordBean.getRetypePaasword());
		nimaiLogin.setToken(null);
		nimaiLogin.setTokenExpiryDate(null);
		loginRepository.save(nimaiLogin);
		return nimaiLogin;
	}

	@Override
	public boolean checkSignIncrendentials(LoginRequest loginRequestBean) {
		Optional<NimaiMLogin> login = loginRepository.findByUserId(loginRequestBean.getUserId());
		NimaiMLogin loginCredentilas = login.get();
		System.out.println(loginCredentilas);
		if ((!loginRequestBean.getUserId().equals(loginCredentilas.getUserid().getUserid())
				|| (!loginRequestBean.getPassword().equals(loginCredentilas.getPassword())))) {
			return false;
		}

		return true;
	}

	@Override
	public String checkUserStatus(String loginRequestBean) {
		Optional<NimaiMLogin> login = loginRepository.findByUserId(loginRequestBean);
		NimaiMLogin loginCredentials = login.get();
		String is_Act_Passed_Status = loginCredentials.getIsActPassed();

		return is_Act_Passed_Status;
	}

	@Override
	public NimaiMLogin saveChangedPasswordDetails(ChangePasswordBean changePasswordBean) {
		
		Optional<NimaiMLogin> login = loginRepository.findByUserId(changePasswordBean.getUserId());
		NimaiMLogin nimaiLogin = login.get();
		nimaiLogin.setPassword(changePasswordBean.getRetypePaasword());
		
		Calendar cal = Calendar.getInstance();
		Date insertedDate = cal.getTime();
		nimaiLogin.setModifiedDate(insertedDate);
		
		loginRepository.save(nimaiLogin);
		return nimaiLogin;
	}
}
