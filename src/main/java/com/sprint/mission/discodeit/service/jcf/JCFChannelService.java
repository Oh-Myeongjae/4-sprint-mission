package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        data = new HashMap<>();
    }

    @Override
    public Channel createChannel(String name) {
        Channel channel = new Channel(name);
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel getChannel(Channel channel) {
        validateChannel(channel);
        return data.get(channel.getId());
    }

    @Override
    public List<Channel> getAllChannel() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateChannel(Channel channel, String name) {
        validateChannel(channel);
        Channel updateChannel = data.get(channel.getId());

        updateChannel.updateName(name);
        data.put(channel.getId(), updateChannel);

    }


    @Override
    public void deleteChannel(Channel channel) {
        validateChannel(channel);
        data.remove(channel.getId());
    }

    @Override
    public void enterChannel(User user, Channel channel) {
        if (!channel.getUsers().contains(user)) {
            channel.addUser(user);
            user.addChannel(channel);
        }
    }

    @Override
    public void leaveChannel(User user, Channel channel) {
        if (channel.getUsers().contains(user)) {
            user.removeChannel(channel);
            channel.removeUser(user);
        }
    }

    @Override
    public void sendMessage(Channel channel, Message message) {
        if (channel.getMessages().contains(message)) {
            channel.addMessage(message);
            message.getSender().addChannel(channel);
        }
    }

    @Override
    public void deleteMessage(Channel channel, Message message) {
        if (channel.getMessages().contains(message)) {
            channel.removeMessage(message);
            message.getSender().removeChannel(channel);
        }
    }

    @Override
    public List<User> getUsers(Channel channel) {
        return new ArrayList<>(channel.getUsers());
    }

    @Override
    public List<Message> getMessages(Channel channel) {
        return new ArrayList<>(channel.getMessages());
    }

    public void validateChannel(Channel channel) {
        if (!data.containsKey(channel.getId())) {
            throw new IllegalArgumentException("유효하지 않은 채널입니다.");
        }
    }
}
