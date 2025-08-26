package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  @Transactional
  @Override
  public ReadStatusDto create(ReadStatusCreateRequest request) {
    UUID userId = request.userId();
    UUID channelId = request.channelId();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("Creating readStatus for user with id {} failed: User not found.", userId);
          return new UserNotFoundException(ErrorCode.USER_NOT_FOUND, userId);
        });
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> {
          log.warn("Creating readStatus for channel with id {} failed: Channel not found.",
              channelId);
          return new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, channelId);
        });

    if (readStatusRepository.existsByUserIdAndChannelId(user.getId(), channel.getId())) {
      log.warn(
          "Creating readStatus for user with id {} and channel with id {} failed: ReadStatus already exists.",
          userId, channelId);
      throw new DiscodeitException(ErrorCode.READSTATUS_ALREADY_EXISTS,
          Map.of("userId", userId.toString(), "channelId", channelId.toString()));
    }

    Instant lastReadAt = request.lastReadAt();
    ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt);
    readStatusRepository.save(readStatus);

    log.info("Created readStatus for user with id {} and channel with id {}", userId, channelId);
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public ReadStatusDto find(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .map(readStatusMapper::toDto)
        .orElseThrow(
            () -> {
              log.warn("ReadStatus with id {} not found", readStatusId);
              return new DiscodeitException(ErrorCode.READSTATUS_NOT_FOUND,
                  Map.of("readStatusId", readStatusId.toString()));
            });
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
    Instant newLastReadAt = request.newLastReadAt();
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> {
          log.warn("Updating readStatus with id {} failed: ReadStatus not found.", readStatusId);
          return new DiscodeitException(ErrorCode.READSTATUS_NOT_FOUND,
              Map.of("readStatusId", readStatusId.toString()));
        });
    readStatus.update(newLastReadAt);
    log.info("Updating readStatus with id {} to lastReadAt {}", readStatusId, newLastReadAt);

    return readStatusMapper.toDto(readStatus);
  }

  @Transactional
  @Override
  public void delete(UUID readStatusId) {
    if (!readStatusRepository.existsById(readStatusId)) {
      log.warn("Deleting readStatus with id {} failed: ReadStatus not found.", readStatusId);
      throw  new DiscodeitException(ErrorCode.READSTATUS_NOT_FOUND,
          Map.of("readStatusId", readStatusId.toString()));
    }

    log.info("Deleting readStatus with id {}", readStatusId);
    readStatusRepository.deleteById(readStatusId);
  }
}
