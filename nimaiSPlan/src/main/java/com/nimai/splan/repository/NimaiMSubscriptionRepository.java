package com.nimai.splan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nimai.splan.model.NimaiTempSubscription;

@Repository
public interface NimaiMSubscriptionRepository extends JpaRepository<NimaiTempSubscription, String> {

	@Query("FROM NimaiMSubscription r where r.subscriptionId = :subscriptionId")
	NimaiTempSubscription findBySubscriptionID(String subscriptionId);


}
