package com.jbd.spring.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

public class SpringBootValueDemoApp implements CommandLineRunner {

	@Value("${app.title}")
	private String appTitle;
	
	@Value("${app.description}")
	private String appDesc;
	
	// Injecting value as LocalDate and LocalDaTetime
	@Value("${default.date}")
	private LocalDate defaultDate;
					
	@Value("${default.time}")
	private LocalDateTime defaultTime;
		
	public static void main(String[] args) {
		SpringApplication.run(SpringBootValueDemoApp.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		
		 System.err.println(appTitle);
		 System.err.println(appDesc);
		
		// Inject Localdate and LocaldateTime
		 System.out.println("\n=============== Inject LocalDate and LocalDatetime using @Value ============");
		 System.err.println(defaultDate);
		 System.err.println(defaultTime);
	}
}
