package com.nimai.splan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nimai.splan.model.NimaiTempSubscription;
import com.nimai.splan.model.NimaiSubscriptionDetails;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<NimaiSubscriptionDetails, Integer> {

	@Query("FROM NimaiSubscriptionDetails n where n.userid.userid = :userId")
	List<NimaiSubscriptionDetails> findAllByUserId(String userId);

	@Query("FROM NimaiSubscriptionDetails n where n.userid.userid = :userId")
	NimaiSubscriptionDetails findById(String userId);
}
