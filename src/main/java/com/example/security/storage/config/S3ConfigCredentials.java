package com.example.security.storage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3ConfigCredentials {

  @Value("${aws.access.key.id}")
  private String awsAccessKeyId;

  @Value("${aws.access.secret.key}")
  private String awsAccessSecretKey;

  @Value("${aws.region}")
  private String awsRegion;


  @Bean
  public S3Client s3Client() {

    AwsBasicCredentials credentials = AwsBasicCredentials.create(
      awsAccessKeyId,
      awsAccessSecretKey
    );

    return S3Client.builder()
                   .credentialsProvider(StaticCredentialsProvider.create(credentials))
                   .region(Region.of(awsRegion))
                   .build();
  }
}
