package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class User extends BaseEntity{
    private String nickName;
    private final List<Channel> channels;
    private final List<Message> messages;

    public User(String nickName) {
        super();
        this.nickName = nickName;
        this.channels = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public String getNickName() {
        return nickName;
    }

    public List<Channel> getChannels() {return channels;}

    public List<Message> getMessages() {return messages;}

    public void addChannel(Channel channel) {
        if(!channels.contains(channel)) {
            channels.add(channel);
            this.setUpdatedAt();
        }
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
    }

    public void addMessage(Message message) {
        messages.add(message);
        this.setUpdatedAt();
    }

    public void removeMessage(Message message) {
        messages.remove(message);
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
        this.setUpdatedAt();
    }

    @Override
    public String toString() {
        return "User{nickName=" + nickName + "}";
    }
}
