package com.nimai.uam.service;

import com.nimai.uam.bean.ChangePasswordBean;
import com.nimai.uam.bean.LoginRequest;
import com.nimai.uam.bean.ResetPasswordBean;
import com.nimai.uam.entity.NimaiMLogin;

public interface UserService {

	boolean checkUserId(String userId);

	boolean checkEmailId(String emailId);

	boolean updateResetToken(ResetPasswordBean resetPasswordBean);

	NimaiMLogin getUserDetailsByTokenKey(String token);

	boolean checkUserTokenKey(String tokenKey);

	NimaiMLogin saveResetPasswordDetails(ResetPasswordBean resetPasswordBean);

	NimaiMLogin saveChangedPasswordDetails(ChangePasswordBean changePasswordBean);

	
	boolean checkSignIncrendentials(LoginRequest loginRequestBean);

	String checkUserStatus(String string);

}
