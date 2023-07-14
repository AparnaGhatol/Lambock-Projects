package com.jbd.spring.demo;

import java.util.Arrays;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.jbd.spring.bean.BasicValuesBean;
import com.jbd.spring.bean.ExampleBean;
import com.jbd.spring.bean.SampleBean;
import com.jbd.spring.bean.ValuesBean;
import com.jbd.spring.config.ValuesBeansConfig;

public class ValuesInjectionDemo {

	public static void main(String[] args) {
		
		System.setProperty("USERNAME", "JAMES");
		
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ValuesBeansConfig.class);
		ExampleBean example = applicationContext.getBean(ExampleBean.class);
		SampleBean sample = applicationContext.getBean(SampleBean.class);
		BasicValuesBean basicValBean = applicationContext.getBean(BasicValuesBean.class);
		ValuesBean valuesBean = applicationContext.getBean(ValuesBean.class);
		
		
		//@Value using with Constructor Injection
		System.out.println("\n=============== @Value with Constructor Injection ==============");
		System.out.println(example.getName());
		System.out.println(example.getSampleBean().getGptRolesList());
		
		// Accessing Beans with @Value 
		System.out.println("\n=============== Accessing Beans with @Value ==============");
		System.out.println(example.getAnotherBeanId());
		System.out.println(example.getAnotherBeanPrice());
		
		
		//@Value using with Setter Injection
		System.out.println("\n=============== @Value with Setter Injection ==============");
		System.out.println(sample.getGptRolesList());
		System.out.println(sample.getBasicValuesBean().getName());
		
		//Basic Valuse Bean
		System.out.println("\n=============== @Value Assign Basic Values ==============");
		System.out.println(basicValBean.getName());
		System.out.println(basicValBean.getId());
		System.out.println(basicValBean.getPrice());
		System.out.println(basicValBean.isActive());
		
		//Basic Usage - Injecting values from properties
		System.out.println("\n=============== Basic Usage - Injecting values from properties ==============");
		System.out.println(valuesBean.getId());
		System.out.println(valuesBean.getProduct());
		System.out.println(valuesBean.getPrice());
		System.out.println(valuesBean.isActiveProduct());
		
		// Injecting with Default values
		System.out.println("\n=============== @Value with default values ==============");
		System.out.println(valuesBean.getNoProperty());
		System.out.println(valuesBean.getUnknonVale());
		
		// Injecting Array, List, Set
		System.out.println("\n=============== @Value: injecting values into  Array, List, Set ==============");
		System.out.println(Arrays.asList(valuesBean.getRolesArray()));
		System.out.println(valuesBean.getRolesList());
		System.out.println(valuesBean.getRolesSet());
		
		// Injecting Ssytem properties and Environment variables
		System.out.println("\n=============== @Value: injecting System, environment properties ==============");
		System.out.println(valuesBean.getSystemUser());
		System.out.println(valuesBean.getEnvionmentUser());
		
		// Injecting Value as Enum
		System.out.println("\n=============== @Value: injecting value as Enum ==============");
		System.out.println(valuesBean.getRole().name() );
		
		
		// Injecting Value as Date and Time
		System.out.println("\n=============== @Value: injecting Localdate and LocaldateTime ==============");
		System.out.println(valuesBean.getDefaultDate());
		System.out.println(valuesBean.getDefaultTime());
		
		
		/* SpEL Examples */
		
		// Iinject System Properties
		System.out.println("\n=============== SpEL with @Value: inject System Properties ==============");
		System.out.println(valuesBean.getSpelUername());
		System.out.println(valuesBean.getSpelEnvUser());
		System.out.println(valuesBean.getSpelNoEnvValue());
		System.out.println(valuesBean.getSpelSysPropsMap());
		
		// Iinjecting List, Set Array with  SpEL
		System.out.println("\n=============== SpEL with @Value: inject List, Set, Array ==============");
		System.out.println(valuesBean.getSpelList());
		System.out.println(Arrays.asList(valuesBean.getSpelArray()));
		System.out.println(valuesBean.getSpelSet());
		
		// Iinjecting List, Set Array with  SpEL
		System.out.println("\n=============== SpEL with @Value: inject Map ==============");
		System.out.println(valuesBean.getSpelSeverirtyMap());
		System.out.println(valuesBean.getSpelAnotherMap());
		System.out.println(valuesBean.getSpelMapWithList());
	
	}

}
