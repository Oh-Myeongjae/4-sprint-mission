package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Transactional
  @Override
  public UserStatusDto create(UserStatusCreateRequest request) {
    UUID userId = request.userId();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("Creating userStatus for user with id {} failed: User not found.", userId);
          return new UserNotFoundException(ErrorCode.USER_NOT_FOUND, userId);
        });
    Optional.ofNullable(user.getStatus())
        .ifPresent(status -> {
          log.warn("Creating userStatus for user with id {} failed: UserStatus already exists.",
              userId);
          throw new DiscodeitException(ErrorCode.USERSTATUS_ALREADY_EXISTS,
              Map.of("userId", userId.toString()));
        });

    Instant lastActiveAt = request.lastActiveAt();
    UserStatus userStatus = new UserStatus(user, lastActiveAt);
    userStatusRepository.save(userStatus);
    log.info("Created userStatus for user with id {}", userId);

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto find(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .map(userStatusMapper::toDto)
        .orElseThrow(
            () -> new DiscodeitException(ErrorCode.USERSTATUS_NOT_FOUND,
                Map.of("userStatusId", userStatusId.toString())));
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> {
          log.warn("Updating userStatus with id {} failed: UserStatus not found.", userStatusId);
          return new DiscodeitException(ErrorCode.USERSTATUS_NOT_FOUND,
              Map.of("userStatusId", userStatusId.toString()));
        });
    userStatus.update(newLastActiveAt);

    log.info("Updated userStatus with id {}", userStatusId);
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> {
          log.warn("Updating userStatus for user with id {} failed: UserStatus not found.", userId);
          return new DiscodeitException(ErrorCode.USERSTATUS_NOT_FOUND,
              Map.of("userId", userId.toString()));
        });
    userStatus.update(newLastActiveAt);

    log.info("Updated userStatus for user with id {}", userId);
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public void delete(UUID userStatusId) {
    if (!userStatusRepository.existsById(userStatusId)) {
      log.warn("Deleting userStatus with id {} failed: UserStatus not found.", userStatusId);
      throw new DiscodeitException(ErrorCode.USERSTATUS_NOT_FOUND,
          Map.of("userStatusId", userStatusId.toString()));
    }

    log.info("Deleting userStatus with id {}", userStatusId);
    userStatusRepository.deleteById(userStatusId);
  }
}
