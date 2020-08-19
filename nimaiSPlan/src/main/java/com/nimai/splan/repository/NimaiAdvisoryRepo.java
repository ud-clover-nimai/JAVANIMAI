package com.nimai.splan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nimai.splan.model.NimaiAdvisory;

@Repository
public interface NimaiAdvisoryRepo extends JpaRepository<NimaiAdvisory,String >{

}
