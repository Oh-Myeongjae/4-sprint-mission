package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  List<ReadStatus> findAllByUserId(UUID userId);

  List<ReadStatus> findAllByChannelId(UUID channelId);

  boolean existsById(UUID id);

  void deleteAllByChannelId(UUID channelId);

    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);
}
