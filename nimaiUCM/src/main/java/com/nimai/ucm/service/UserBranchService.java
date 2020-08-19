package com.nimai.ucm.service;

import com.nimai.ucm.bean.BranchUserBean;
import com.nimai.ucm.bean.UserBranchBean;

public interface UserBranchService {

	String saveUserBranchDetails(UserBranchBean branchUserBean, String userid);
}
