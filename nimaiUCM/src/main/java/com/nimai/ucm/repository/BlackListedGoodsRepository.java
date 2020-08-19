package com.nimai.ucm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nimai.ucm.entity.BlackListedGoods;
import com.nimai.ucm.entity.NimaiCustomer;

public interface BlackListedGoodsRepository extends JpaRepository<BlackListedGoods, Long> {

	List<BlackListedGoods> findByUserId(NimaiCustomer nc);
	
	

}
