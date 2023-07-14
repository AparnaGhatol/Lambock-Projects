package com.jbd.spring.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class ExampleBean {
	
	private final String name;
	
	private final SampleBean sampleBean;
	
	@Autowired
	public ExampleBean(@Value("${example.bean.name}") String name, SampleBean sampleBean) {
		this.name = name;
		this.sampleBean = sampleBean;
	}
	
	@Value("#{basicValuesBean.price}")
	private double anotherBeanPrice;
	
	@Value("#{basicValuesBean.getId()}")
	private int anotherBeanId;

}
