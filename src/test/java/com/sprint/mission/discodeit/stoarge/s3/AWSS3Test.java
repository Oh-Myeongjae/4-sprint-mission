package com.sprint.mission.discodeit.stoarge.s3;

import com.sprint.mission.discodeit.storage.s3.AwsProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AWSS3Test {

  @Autowired
  private S3Client s3Client;

  @Autowired
  private S3Presigner s3Presigner;

  @Autowired
  private AwsProperties awsProperties;

  /**
   * 업로드 테스트
   */
  @Test
  void testUpload() {
    String bucket = awsProperties.getS3().getBucket();
    String key = "test/" + UUID.randomUUID() + ".txt";
    String content = "Hello S3 Upload Test!";

    // 업로드
    s3Client.putObject(
        PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build(),
        RequestBody.fromString(content)
    );

    // ✅ 검증: 업로드한 객체가 버킷에 존재하는지 확인
    boolean exists = s3Client.listObjectsV2(b -> b.bucket(bucket))
        .contents()
        .stream()
        .anyMatch(o -> o.key().equals(key));
    assertTrue(exists, "업로드한 객체가 버킷에 존재해야 합니다.");
  }

  /**
   * 다운로드 테스트
   */
  @Test
  void testDownload() throws IOException {
    String bucket = awsProperties.getS3().getBucket();
    String key = "test/00d84807-eafd-47c0-b540-c213b0977838.txt"; // 미리 업로드된 키
    Path downloadPath = Path.of("build/tmp/downloaded-" + UUID.randomUUID() + ".txt");

    // 다운로드
    s3Client.getObject(
        GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build(),
        downloadPath
    );

    String downloadedContent = Files.readString(downloadPath);

    // ✅ 검증: 파일이 존재하고 내용이 비어있지 않은지 확인
    assertTrue(Files.exists(downloadPath), "다운로드된 파일이 존재해야 합니다.");
    assertNotNull(downloadedContent, "다운로드된 내용이 null이면 안 됩니다.");
    assertFalse(downloadedContent.isEmpty(), "다운로드된 내용이 비어있으면 안 됩니다.");
  }


  /**
   * 프리사인드 URL 생성 테스트
   */
  @Test
  void testPresignedUrl() {
    String bucket = awsProperties.getS3().getBucket();
    String key = "test/00d84807-eafd-47c0-b540-c213b0977838.txt";

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(5))
        .getObjectRequest(b -> b.bucket(bucket).key(key))
        .build();

    PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(presignRequest);

    // ✅ 검증: URL이 null이 아니고 https로 시작하는지 확인
    assertNotNull(presigned.url(), "Presigned URL은 null이 아니어야 합니다.");
    assertTrue(presigned.url().toString().startsWith("https://"), "URL은 HTTPS로 시작해야 합니다.");
  }

  /**
   * 통합 테스트: 업로드 후 다운로드 및 내용 검증
   */
  @Test
  void testS3UploadDownloadIntegration() throws IOException {
    String bucket = awsProperties.getS3().getBucket();
    String key = "test/" + UUID.randomUUID() + ".txt";
    String content = "Hello S3 Integration Test!";

    // 1️⃣ 업로드
    s3Client.putObject(
        PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build(),
        RequestBody.fromString(content)
    );

    // 2️⃣ 다운로드
    Path downloadPath = Path.of("build/tmp/downloaded-" + UUID.randomUUID() + ".txt");
    s3Client.getObject(
        GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build(),
        downloadPath
    );

    // 3️⃣ 검증
    String downloadedContent = Files.readString(downloadPath);
    assertEquals(content, downloadedContent, "업로드한 내용과 다운로드한 내용이 일치해야 합니다.");

    // 4️⃣ 추가 검증: 파일이 실제로 존재하는지 확인
    assertTrue(Files.exists(downloadPath), "다운로드된 파일이 존재해야 합니다.");
  }

}
