package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface ChannelService {
    Channel createChannel(String name);

    Channel getChannel(Channel channel);
    List<Channel> getAllChannel();

    void updateChannel(Channel channel, String name);

    void deleteChannel(Channel channel);

    void enterChannel(User user, Channel channel);
    void leaveChannel(User user, Channel channel);

    void sendMessage(Channel channel, Message message);
    void deleteMessage(Channel channel, Message message);

    List<User> getUsers(Channel channel);
    List<Message> getMessages(Channel channel);
}
