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
    public Channel updateChannel(Channel channel, String name) {
        validateChannel(channel);
        Channel updateChannel = data.get(channel.getId());

        updateChannel.updateName(name);
        data.put(channel.getId(), updateChannel);

        return updateChannel;
    }


    @Override
    public void deleteChannel(Channel channel) {
        validateChannel(channel);
        data.remove(channel.getId());
    }

    @Override
    public void enterChannel(User user, Channel channel) {
        channel.addUser(user);
    }

    @Override
    public void leaveChannel(User user, Channel channel) {
        channel.removeUser(user);
    }

    @Override
    public void sendMessage(Channel channel, Message message) {
        channel.addMessage(message);
    }

    @Override
    public void deleteMessage(Channel channel, Message message) {
        channel.removeMessage(message);
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
