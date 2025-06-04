package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class Channel extends BaseEntity {
    private final List<User> users;
    private final List<Message> messages;
    private String name;

    public Channel(String name) {
        super();
        this.users = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
        this.setUpdatedAt();
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
        this.setUpdatedAt();
    }

    public void removeUser(User user) {
        users.remove(user);
        this.setUpdatedAt();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
        this.setUpdatedAt();
    }

    public void removeMessage(Message message) {
        messages.remove(message);
        this.setUpdatedAt();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "users=" + users +
                ", messages=" + messages +
                ", name='" + name + '\'' +
                '}';
    }
}
