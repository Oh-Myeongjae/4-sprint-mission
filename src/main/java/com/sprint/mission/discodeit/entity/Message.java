package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;
    private final User sender;
    private final Channel channel;

    public Message(Channel channel,String message, User sender) {
        super();
        this.content = message;
        this.channel = channel;
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public User getSender() {
        return sender;
    }

    public Channel getChannel() {return channel;}

    public void updateMessage(String message) {
        this.content = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", sender=" + sender +
                ", channel=" + channel.getName() +
                '}';
    }
}
