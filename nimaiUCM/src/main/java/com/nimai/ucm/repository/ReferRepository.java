package com.nimai.ucm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nimai.ucm.entity.Refer;

@Repository
public interface ReferRepository extends JpaRepository<Refer, String> {

	@Query("select re from Refer re where re.userId= (:userId)")
	List<Refer> findReferByUserId(@Param("userId") String userId);

	void existsByEmailAddress(String emailAddress);

}
