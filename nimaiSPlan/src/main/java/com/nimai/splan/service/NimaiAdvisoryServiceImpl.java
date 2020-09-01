package com.nimai.splan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimai.splan.model.NimaiAdvisory;
import com.nimai.splan.repository.NimaiAdvisoryRepo;

@Service
public class NimaiAdvisoryServiceImpl implements NimaiAdvisoryService {

	@Autowired
	NimaiAdvisoryRepo nimaiAdvisoryRepo;

	@Override
	public List<NimaiAdvisory> viewAdvisory() {
		// TODO Auto-generated method stub
		return nimaiAdvisoryRepo.findAll();
	}

	@Override
	public List<NimaiAdvisory> viewAdvisoryByCountry(String country_name) {

		List<NimaiAdvisory> refer = nimaiAdvisoryRepo.findByCountryName(country_name);
		return refer;

	}

}
