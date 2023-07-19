package com.jbd.thymeleaf_start.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.thymeleaf.dialect.springdata.SpringDataDialect;


@Configuration
@EntityScan("com.jbd.thymeleaf_start.domain")
@EnableJpaRepositories("com.jbd.thymeleaf_start.repos")
@EnableTransactionManagement
public class DomainConfig {

	@Bean
    public SpringDataDialect springDataDialect() {
        return new SpringDataDialect();
    }
	
}
