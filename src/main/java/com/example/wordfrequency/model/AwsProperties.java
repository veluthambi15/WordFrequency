package com.example.wordfrequency.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties class for AWS credentials.
 */
@Component
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {

    private String accessKey;
    private String secretKey;

    /**
     * Getter method for retrieving the AWS access key.
     * @return String - AWS access key
     */
    public String getAccessKey() {
        return accessKey;
    }
    
    /**
     * Setter method for setting the AWS access key.
     * @param accessKey String - AWS access key to set
     */
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    /**
     * Getter method for retrieving the AWS secret key.
     * @return String - AWS secret key
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * Setter method for setting the AWS secret key.
     * @param secretKey String - AWS secret key to set
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}

