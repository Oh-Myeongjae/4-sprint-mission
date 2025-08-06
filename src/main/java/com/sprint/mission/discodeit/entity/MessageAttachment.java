package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

//@Entity
//@Table(name = "message_attachments")
public class MessageAttachment extends BaseUpdatableEntity {

  @ManyToOne
  @JoinColumn(name = "message_id", nullable = false)
  private Message message;

  @ManyToOne
  @JoinColumn(name = "attachment_id", nullable = false)
  private BinaryContent attachment;
}
