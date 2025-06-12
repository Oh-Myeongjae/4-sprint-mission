package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface MessageService {
    Message create(Channel channel, String message, User sender);

    Message getMessage(Message message);
    List<Message> getAllMessages();

    Message update(Message message, String content);

    void delete(Message message);

    void validationMessage(Message message);
}
