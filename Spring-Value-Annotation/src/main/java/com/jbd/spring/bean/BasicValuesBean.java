package com.jbd.spring.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class BasicValuesBean {
	
	@Value("James")
	private String name;
	
	@Value("1001")
	private int id;
	
	@Value("19.91")
	private double price;
	
	@Value("true")
	private boolean active;

}
