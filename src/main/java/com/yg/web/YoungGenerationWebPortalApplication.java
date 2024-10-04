package com.yg.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class YoungGenerationWebPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(YoungGenerationWebPortalApplication.class, args);
	}

}
