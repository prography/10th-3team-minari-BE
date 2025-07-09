package com.prography.minari.common.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {
    /**
     * todo
     * 아직 properties에 값 안넣음
     */
    @Value("${cloud.aws.credentials.access-key:default}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key:default}")
    private String secretKey;
    @Value("${cloud.aws.region.static:default}")
    private String region;

    /**
     * Creates and configures an {@link AmazonS3Client} bean using AWS credentials and region from application properties.
     *
     * @return a configured {@link AmazonS3Client} for interacting with AWS S3
     */
    @Bean
    public AmazonS3Client amazonS3Client() {
         BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
