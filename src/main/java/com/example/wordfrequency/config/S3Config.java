package com.example.wordfrequency.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

	// Injecting AWS region from application properties
	@Value("${cloud.aws.region.static}")
    private String awsRegion;
	
	 /**
     * Configures and provides an instance of the Amazon S3 client.
     * @return S3Client - Amazon S3 client instance
     */	
    @Bean
    public S3Client s3Client() {
    	return S3Client.builder()
    			.region(Region.of(awsRegion))  // Set the AWS region
                .credentialsProvider(DefaultCredentialsProvider.create())  // Use default credentials provider
                .build();
    }
    
}