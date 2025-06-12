package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public interface ChannelRepository {
    void setData();
    void setData(Channel channel);
    List<Channel> getAllChannels();
    Channel getChannel(Channel channel);
    void removeData(Channel channel);
}
