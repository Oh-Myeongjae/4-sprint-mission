package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    public JCFMessageService() {
        data = new HashMap<>();
    }


    @Override
    public Message create(Channel channel, String content, User sender) {
        Message message = new Message(channel,content, sender);
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message getMessage(Message message) {
        validationMessage(message);
        return data.get(message.getId());
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(Message message, String content) {
        validationMessage(message);
        Message updateMessage = data.get(message.getId());

        updateMessage.updateMessage(content);
        data.put(message.getId(), updateMessage);

        return updateMessage;
    }


    @Override
    public void delete(Message message) {
        validationMessage(message);
        data.remove(message.getId());
    }

    public void  validationMessage(Message message){
        if(!data.containsKey(message.getId())){
            throw new IllegalArgumentException("유효하지 않은 메시지입니다.");
        }
    }
}
