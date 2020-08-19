package com.nimai.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration(exclude = HibernateJpaAutoConfiguration.class)
@EnableScheduling
public class NimaiEmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(NimaiEmailApplication.class, args);
		System.out.println(" =========== NIMAI EMAIL SERVICE =========== ");
	}
}
