package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity{
    private String content;
    private final User sender;

    public Message(String message, User sender) {
        super();
        this.content = message;
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public User getSender() {
        return sender;
    }

    public void updateMessage(String message) {
        this.content = message;
    }
}
