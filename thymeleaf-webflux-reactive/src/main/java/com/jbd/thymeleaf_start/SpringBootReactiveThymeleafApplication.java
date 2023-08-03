package com.jbd.thymeleaf_start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpringBootReactiveThymeleafApplication {
	
    public static void main(final String[] args) {
    
      SpringApplication app = new SpringApplication(SpringBootReactiveThymeleafApplication.class);
      app.setWebApplicationType(WebApplicationType.REACTIVE);
      app.run(args);
    }

}
