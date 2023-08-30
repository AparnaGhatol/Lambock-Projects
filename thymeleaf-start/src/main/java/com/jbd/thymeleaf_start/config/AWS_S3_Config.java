package com.jbd.thymeleaf_start.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AWS_S3_Config {
	
	@Value("${cloud.s3.credentials.accessKey}")
    private String awsId;

    @Value("${cloud.s3.credentials.secretKey}")
    private String awsKey;

    @Value("${cloud.s3.region.name}")
    private String region;
    
    @Value("${cloud.s3.endpoint}")
    private String endpoint;
    
    @Bean("s3storage")
    public AmazonS3 s3client() {

        BasicAWSCredentials creds = new BasicAWSCredentials(awsId, awsKey);
		return AmazonS3ClientBuilder
				.standard()
				.withEndpointConfiguration(new EndpointConfiguration(endpoint, region))
				.withCredentials(new AWSStaticCredentialsProvider(creds)).build();

    }

}
