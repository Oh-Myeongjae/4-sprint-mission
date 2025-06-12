package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class FileMessageService implements MessageService {

    private static final String FILE_PATH = "data/messages.csv";
    private final Map<UUID, Message> messageData = new HashMap<>();

    @Override
    public Message create(Channel channel, String message, User sender) {
        Message newMessage = new Message(channel, message, sender);

        messageData.put(newMessage.getId(), newMessage);
        channel.addMessage(newMessage);
        sender.addMessage(newMessage);

        saveMessage();
        return newMessage;
    }

    @Override
    public Message getMessage(Message message) { // 특정 메시지 조회
        validationMessage(message);
        return messageData.get(message.getId());
    }

    @Override
    public List<Message> getAllMessages() { // 모든 메시지 목록 반환
        return new ArrayList<>(messageData.values());
    }

    @Override
    public Message update(Message message, String content) { // 메시지 내용 업데이트
        validationMessage(message);

        Message updateMessage = messageData.get(message.getId());
        updateMessage.updateMessage(content);
        messageData.put(message.getId(), updateMessage);

        saveMessage();
        return updateMessage;
    }

    @Override
    public void delete(Message message) { // 메시지 삭제
        validationMessage(message);

        message.getSender().removeMessage(message);
        messageData.remove(message.getId());

        saveMessage();
    }

    private void saveMessage() {
        try {
            FileOutputStream fos = new FileOutputStream(FILE_PATH);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(messageData);

        } catch (IOException e) {
            System.out.println("메세지 정보 저장에 실패했습니다.");
        }
    }

    public void validationMessage(Message message) { // 메시지 존재 여부 검증
        if (!messageData.containsKey(message.getId())) {
            throw new NoSuchElementException("유효하지 않은 메시지입니다.");
        }
    }
}
