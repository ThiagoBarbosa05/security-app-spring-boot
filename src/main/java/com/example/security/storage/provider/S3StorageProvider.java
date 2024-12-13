package com.example.security.storage.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Slf4j
@Component
public class S3StorageProvider implements StorageProvider {

  @Value("${aws.s3.bucket.name}")
  private String awsS3BucketName;

  private final S3Client s3Client;

  public S3StorageProvider(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  @Override
  public void upload(MultipartFile file, String key) {
    try {
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(awsS3BucketName)
        .key(key)
        .contentType(file.getContentType()).build();

      s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
    } catch (IOException exception) {
      log.info(">>>>> Upload Error >>>>>> ", exception);
      throw new RuntimeException("Error when uploading the file >>>>", exception);
    }
  }

  @Override
  public void delete(String key) {
      DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
        .bucket(awsS3BucketName)
        .key(key)
        .build();

      s3Client.deleteObject(deleteObjectRequest);
  }
}
