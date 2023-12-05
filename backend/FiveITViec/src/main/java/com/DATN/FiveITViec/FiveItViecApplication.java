package com.DATN.FiveITViec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EntityScan("com.DATN.FiveITViec.model")
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class FiveItViecApplication {

	public static void main(String[] args) {
		SpringApplication.run(FiveItViecApplication.class, args);
	}
}
