package com.nimai.splan.service;

import java.util.List;

import com.nimai.splan.model.NimaiAdvisory;


public interface NimaiAdvisoryService {
		public List<NimaiAdvisory> viewAdvisory();
		
		public List<NimaiAdvisory> viewAdvisoryByCountry(String country_name);
}
