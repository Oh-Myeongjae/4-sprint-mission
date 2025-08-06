package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByUsername(String username);

  boolean existsById(UUID id);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);
}
