package com.jbd.thymeleaf_start.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.thymeleaf.dialect.springdata.SpringDataDialect;


@Configuration
public class DomainConfig {
	
	@Bean
    public SpringDataDialect springDataDialect() {
        return new SpringDataDialect();
    }

}
