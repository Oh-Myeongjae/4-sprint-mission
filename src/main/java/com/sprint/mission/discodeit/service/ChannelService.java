package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public interface ChannelService {
    Channel createChannel(String name);
    Channel getChannel(Channel channel);
    List<Channel> getAllChannel();
    Channel updateChannel(Channel channel, String name);
    void deleteChannel(Channel channel);
}
