package com.jbd.spring.bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jbd.spring.util.Roles;

import lombok.Getter;

@Component
@Getter
public class ValuesBean {

	// Basic Usage
	@Value("${product.id}")
	private int id;
	
	@Value("${product.name}")
	private String product;
	
	@Value("${product.price}")
	private double price;
	
	@Value("${product.status.active}")
	private boolean activeProduct;
	
	
	// Default Values
	@Value("${value.notfound:I am Default Value}")
	private String noProperty;
	
	@Value("${price.unknown: 199}")
	private int unknonVale;
	
	
	// Array, List, Set
	@Value("${gpt.roles}")
	private String[] rolesArray;
		
	@Value("${gpt.roles}")
	private List<String> rolesList;
	
	@Value("${gpt.roles}")
	private Set<String> rolesSet;
	
	
	// System and Environment properties
	@Value("${user.name}")
	private String systemUser;
	
	@Value("${USERNAME}")
	private String envionmentUser;
	
	
	// Injecting value as Enum
	@Value("${user.default.role}")
	private Roles role;
	
	
	/* Spring Expression Language (SpEL) */
	
	// Injecting value as LocalDate and LocalDaTetime
	@Value("#{T(java.time.LocalDate).parse('${app.default.date}')}")
	private LocalDate defaultDate;
				
	@Value("#{T(java.time.LocalDateTime).parse('${app.default.time}')}")
	private LocalDateTime defaultTime;
	
	
	// Injecting System properties using SpEL
	@Value("#{systemProperties['user.name']}")
	private String spelUername;
					
	@Value("#{systemEnvironment['USERNAME']}")
	private String spelEnvUser;
	
	@Value("#{systemEnvironment['NOVALUE'] ?: 'myvalue'}")
	private String spelNoEnvValue;
	
	@Value("#{systemProperties}")
	private Map<String, String> spelSysPropsMap;
	
	
	// Injecting List by split with comma
	@Value("#{'${gpt.roles}'.split(',')}")
	private List<String> spelList;
	
	@Value("#{'${gpt.roles}'.split(',')}")
	private String[] spelArray;
	
	@Value("#{'${gpt.roles}'.split(',')}")
	private Set<String> spelSet;
	
	
	// Injecting Map
	@Value("#{${task.severities}}")
	private Map<String, Integer> spelSeverirtyMap;
	
	@Value("#{${app.another.map}}")
	private Map<String, String> spelAnotherMap;
	
	@Value("#{${app.privilege.map}}")
	private Map<String, List<String>> spelMapWithList;
	
}
