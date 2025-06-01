package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface MessageService {
    Message create(String message, User sender);
    Message getById(Message message);
    List<Message> getAll();
    Message update(Message message, String content);
    boolean delete(Message message);
}
