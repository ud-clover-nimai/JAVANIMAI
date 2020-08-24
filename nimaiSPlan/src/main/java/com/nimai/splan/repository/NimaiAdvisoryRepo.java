package com.nimai.splan.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nimai.splan.model.NimaiAdvisory;

@Repository
@Transactional
public interface NimaiAdvisoryRepo extends JpaRepository<NimaiAdvisory,String >
{
	@Query("SELECT na FROM NimaiAdvisory na WHERE na.country_name = (:country_name) and na.status='Active'")
    List<NimaiAdvisory> findByCountryName(@Param("country_name") String country_name);
}
