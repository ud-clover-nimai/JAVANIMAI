package com.nimai.ucm.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.nimai.ucm.bean.ReferBean;
import com.nimai.ucm.entity.Refer;

public interface ReferService {

	public ResponseEntity<?> saveReferService(ReferBean referbean, String r1);

	public boolean checkEmailId(String emailId);

	public List<Refer> viewReferB(ReferBean referbean);

	public Refer updateRefer(ReferBean referbean);

	public List<Refer> getReferByUserId(String userId);
}
