package com.sprint.mission.discodeit.config.security;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final PasswordEncoder passwordEncoder;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final UserMapper userMapper;


  @Value("${role.admin.address}")
  String address;

  @Value("${role.admin.password}")
  String password;

  @Override
  @Transactional
  public void run(String... args) {

    boolean exists = userRepository.existsByRole(Role.ADMIN);

    if (exists) {
      log.info("관리자 계정이 이미 존재합니다. 초기화를 건너뜁니다.");
      return;
    }

    log.info("관리자 계정이 존재하지 않아 초기화합니다.");

    BinaryContent nullableProfile = null;


    String encodedPassword = passwordEncoder.encode(password);

    User admin = new User(
        "admin",
        address,
        encodedPassword,
        null,
        Role.ADMIN
    );

    // UserStatus 생성
    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(admin, now);

    // 저장
    userRepository.save(admin);

    log.info("관리자 계정 초기화 완료: username={}, email={}", admin.getUsername(), admin.getEmail());
  }
}

