package com.jbd.spring.bean;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class SampleBean {

	private List<String> gptRolesList;
	
	private BasicValuesBean basicValuesBean;
	
	// @Value with setter injection
	@Autowired
    public void setValues(@Value("${gpt.roles}") List<String> gptRolesList, BasicValuesBean basicValuesBean) {
        this.gptRolesList = gptRolesList;
        this.basicValuesBean = basicValuesBean;
    }
}
