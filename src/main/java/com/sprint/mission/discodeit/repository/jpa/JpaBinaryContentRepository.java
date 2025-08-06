package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBinaryContentRepository extends JpaRepository<BinaryContent, UUID> {

  List<BinaryContent> findAllByIdIn(List<UUID> ids);

  boolean existsById(UUID id);


}
