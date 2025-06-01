package com.sprint.mission.discodeit.service.jcf;

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
    public Message create(String content, User sender) {
        Message message = new Message(content, sender);
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message getById(Message message) {
        validationMessage(message);
        return data.get(message.getId());
    }

    @Override
    public List<Message> getAll() {
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
    public boolean delete(Message message) {
        validationMessage(message);
        return data.remove(message.getId()) != null;
    }

    public void  validationMessage(Message message){
        if(!data.containsKey(message.getId())){
            throw new IllegalArgumentException("유효하지 않은 메시지입니다.");
        }
    }
}
