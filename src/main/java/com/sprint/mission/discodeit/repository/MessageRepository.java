package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface MessageRepository {
    void setData();
    void setData(Message message);
    List<Message> getAllMessages();
    Message getMessage(Message message);
    void removeData(Message message);
}
