package com.udpr.quot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class QuotApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuotApplication.class, args);
	}

}
