package com.luulsolutions.luulpos.service.s3;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Service
public class S3Service {
    private AmazonS3 amazonS3;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

@PostConstruct
    private void initializeAmazon() {
       AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
       this.setAmazonS3(AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1)
    		   .withCredentials(new AWSStaticCredentialsProvider(credentials)).build());
}

public AmazonS3 getAmazonS3() {
	return amazonS3;
}

public void setAmazonS3(AmazonS3 amazonS3) {
	this.amazonS3 = amazonS3;
}
}