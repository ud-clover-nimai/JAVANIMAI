package com.nimai.ucm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nimai.ucm.entity.InterestedCountry;
import com.nimai.ucm.entity.NimaiCustomer;

public interface InterestedCountryRepository extends JpaRepository<InterestedCountry, Long> {

	List<InterestedCountry> findByUserId(NimaiCustomer nc);

}
