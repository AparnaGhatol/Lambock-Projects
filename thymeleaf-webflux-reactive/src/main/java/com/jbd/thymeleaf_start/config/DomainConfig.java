package com.jbd.thymeleaf_start.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.dialect.springdata.SpringDataDialect;


@Configuration
@EntityScan("com.jbd.thymeleaf_start.domain")
public class DomainConfig {

	@Bean
	 public SpringDataDialect springDataDialect() {
	     return new SpringDataDialect();
	 }
}
