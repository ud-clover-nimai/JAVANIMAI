package com.nimai.splan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.nimai.splan.service.SubscriptionPlanServiceImpl;


@EntityScan(basePackageClasses = { NimaiSPlanApplication.class, Jsr310JpaConverters.class })
@SpringBootApplication
public class NimaiSPlanApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionPlanServiceImpl.class);
	public static void main(String[] args) {
		SpringApplication.run(NimaiSPlanApplication.class, args);
		System.out.println(" =========== NIMAI Subscription and Discounting =========== ");
		LOGGER.info("=========== NIMAI Subscription and Discounting ===========  ");
	}

}
