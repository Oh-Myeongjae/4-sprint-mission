package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserStatusRepository extends JpaRepository<UserStatus, UUID> {

  Optional<UserStatus> findByUserId(UUID userId);

  boolean existsById(UUID id);

  void deleteByUserId(UUID userId);

  boolean existsByUserId(UUID userId);
}
