package com.nimai.uam.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nimai.uam.entity.NimaiClient;
import com.nimai.uam.entity.NimaiMLogin;




@Repository
public interface LoginRepository extends JpaRepository<NimaiMLogin, Long>{

	
	
	
	@Query("FROM NimaiMLogin n where n.userid.userid = :userid")
	Optional<NimaiMLogin> findByUserId(@Param("userid") String userid);

	@Query("FROM NimaiMLogin n where n.token = :token")
	NimaiMLogin findUserByToken(String token);

	@Query("select n.token FROM NimaiMLogin n WHERE n.token=:tokenKey")
	String checkUserToken(String tokenKey);
	
	
	
	
}
