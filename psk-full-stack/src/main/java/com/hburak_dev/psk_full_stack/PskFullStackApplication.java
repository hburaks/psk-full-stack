package com.hburak_dev.psk_full_stack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableJpaAuditing
@EnableAsync
@SpringBootApplication
public class PskFullStackApplication {

	public static void main(String[] args) {
		SpringApplication.run(PskFullStackApplication.class, args);
	}

}
