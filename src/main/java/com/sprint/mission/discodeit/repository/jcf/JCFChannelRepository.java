package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.NoSuchElementException;

public class JCFChannelRepository implements ChannelService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public JCFChannelRepository(UserRepository userRepository, ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel createChannel(User user, String name) {
        Channel newChannel = new Channel(user,name);
        user.addChannel(newChannel);

        channelRepository.setData(newChannel);
        userRepository.setData(user);

        return newChannel;
    }

    @Override
    public Channel getChannel(Channel channel) {
        validateChannel(channel);
        return channelRepository.getChannel(channel);
    }

    @Override
    public List<Channel> getAllChannel() {
        return channelRepository.getAllChannels();
    }

    @Override
    public void updateChannel(Channel channel, String name) {
        validateChannel(channel);

        Channel updateChannel = channelRepository.getChannel(channel);
        updateChannel.updateName(name);

        channelRepository.setData();
        userRepository.setData();
    }

    @Override
    public void deleteChannel(Channel channel) {
        validateChannel(channel);

        channelRepository.removeData(channel);
        channel.getUsers().forEach(user -> user.removeChannel(channel));

        userRepository.setData();
    }

    @Override
    public void enterChannel(User user, Channel channel) {
        validateChannel(channel);

        channel.addUser(user);
        user.addChannel(channel);

        channelRepository.setData(channel);
        userRepository.setData(user);
    }

    @Override
    public void leaveChannel(User user, Channel channel) {
        validateChannel(channel);

        channel.removeUser(user);
        user.removeChannel(channel);

        channelRepository.setData(channel);
        userRepository.setData(user);
    }

    @Override
    public List<User> getUsers(Channel channel) {
        return channelRepository.getChannel(channel).getUsers();
    }

    @Override
    public List<Message> getMessages(Channel channel) {
        return channelRepository.getChannel(channel).getMessages();
    }

    @Override
    public void validateChannel(Channel channel) {
        if (channelRepository.getChannel(channel) == null) {
            throw new NoSuchElementException("유효하지 않은 채널입니다.");
        }
    }
}
