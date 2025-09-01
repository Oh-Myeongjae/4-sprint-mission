package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final String accessKey;
  private final String secretKey;
  private final String region;
  private final String bucket;
  private final long presignedUrlExpiration;

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  public S3BinaryContentStorage(
      @Value("${s3.credentials.access-key}") String accessKey,
      @Value("${s3.credentials.secret-key}") String secretKey,
      @Value("${s3.region}") String region,
      @Value("${s3.bucket}") String bucket,
      @Value("${s3.presigned-url-expiration}") String presignedUrlExpiration
  ) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
    this.region = region;
    this.bucket = bucket;
    this.presignedUrlExpiration = Long.parseLong(presignedUrlExpiration);

    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

    this.s3Client = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();

    this.s3Presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }

  @Override
  public UUID put(UUID uuid, byte[] bytes) {
    String key = uuid.toString();

    PutObjectRequest putReq = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .contentType("application/octet-stream")
        .build();

    s3Client.putObject(putReq,
        RequestBody.fromBytes(bytes));

    return uuid;
  }

  @Override
  public InputStream get(UUID uuid) {
    String key = uuid.toString();

    return s3Client.getObject(GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build());
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
    String key = binaryContentDto.id().toString();

    // presigned url 생성
    URL presignedUrl = generatePresignedUrl(key, binaryContentDto);

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.LOCATION, presignedUrl.toString());

    return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 Redirect
  }

  private URL generatePresignedUrl(String key, BinaryContentDto binaryContentDto) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .responseContentType(binaryContentDto.contentType())
        .responseContentDisposition("attachment; filename=\"" + binaryContentDto.fileName() + "\"")
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .getObjectRequest(getObjectRequest)
        .signatureDuration(Duration.ofSeconds(presignedUrlExpiration))
        .build();

    return s3Presigner.presignGetObject(presignRequest).url();
  }
}
