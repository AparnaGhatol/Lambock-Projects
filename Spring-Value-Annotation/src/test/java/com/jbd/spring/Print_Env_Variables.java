package com.jbd.spring;

import java.util.Map;

public class Print_Env_Variables {

	public static void main(String[] args) {
        Map<String, String> envMap = System.getenv();
        
        for (String envName : envMap.keySet()) {
            System.out.format("%s = %s%n", envName, envMap.get(envName));
        }
    }
}
