package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChannelRepository extends JpaRepository<Channel, UUID> {

  List<Channel> findAll();

  boolean existsById(UUID id);

}
