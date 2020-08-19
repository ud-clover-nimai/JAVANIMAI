package com.nimai.splan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nimai.splan.model.NimaiMCustomer;
import com.nimai.splan.model.NimaiMSubscription;

public interface NimaiMSPlanRepository extends JpaRepository<NimaiMSubscription, String> {

}
