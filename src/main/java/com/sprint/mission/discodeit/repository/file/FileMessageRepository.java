package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messageData;
    private static final String FILE_PATH = "data/messages.csv";

    public FileMessageRepository() {
        messageData = initData();
    }

    public Map<UUID, Message> initData() {
        Map<UUID, Message> map = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            map = (Map<UUID, Message>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("매세지 초기화에 실패했습니다.");
        }

        return map;
    }

    @Override
    public void setData() {
        persist();
    }

    @Override
    public void setData(Message message) {
        messageData.put(message.getId(), message);
        persist();
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(messageData.values());
    }

    @Override
    public Message getMessage(Message message) {
        return messageData.get(message.getId());
    }

    @Override
    public void removeData(Message message) {
        messageData.remove(message.getId());
        persist();
    }

    private void persist() {
        try {
            FileOutputStream fos = new FileOutputStream(FILE_PATH);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(messageData);

        } catch (IOException e) {
            System.out.println("매세지 저장에 실패했습니다.");
        }
    }
}
