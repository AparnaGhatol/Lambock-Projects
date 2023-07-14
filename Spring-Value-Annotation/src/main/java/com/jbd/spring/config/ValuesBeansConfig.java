package com.jbd.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"com.jbd.spring.bean"})
@PropertySource(name = "props", value = "other_props.properties")
public class ValuesBeansConfig {

}
