package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data; // 메시지 데이터를 저장하는 맵

    public JCFMessageService() {
        data = new HashMap<>();
    }

    @Override
    public Message create(Channel channel, String content, User sender) { // 새 메시지 생성 및 저장
        Message message = new Message(channel, content, sender);
        data.put(message.getId(), message);
        sender.addMessage(message);
        return message;
    }

    @Override
    public Message getMessage(Message message) { // 특정 메시지 조회
        validationMessage(message);
        return data.get(message.getId());
    }

    @Override
    public List<Message> getAllMessages() { // 모든 메시지 목록 반환
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(Message message, String content) { // 메시지 내용 업데이트
        validationMessage(message);
        Message updateMessage = data.get(message.getId());
        updateMessage.updateMessage(content);
        data.put(message.getId(), updateMessage);
        return updateMessage;
    }

    @Override
    public void delete(Message message) { // 메시지 삭제
        validationMessage(message);
        message.getSender().removeMessage(message);
        data.remove(message.getId());
    }

    public void validationMessage(Message message) { // 메시지 존재 여부 검증
        if (!data.containsKey(message.getId())) {
            throw new IllegalArgumentException("유효하지 않은 메시지입니다.");
        }
    }
}

