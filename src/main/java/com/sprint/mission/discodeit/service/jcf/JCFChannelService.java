package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data; // 채널 데이터를 저장하는 맵

    public JCFChannelService() {
        data = new HashMap<>();
    }

    @Override
    public Channel createChannel(String name) { // 새 채널을 생성하여 저장
        Channel channel = new Channel(name);
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel getChannel(Channel channel) { // 특정 채널 조회
        validateChannel(channel);
        return data.get(channel.getId());
    }

    @Override
    public List<Channel> getAllChannel() { // 모든 채널 목록 반환
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateChannel(Channel channel, String name) { // 채널 이름 변경
        validateChannel(channel);
        Channel updateChannel = data.get(channel.getId());
        updateChannel.updateName(name);
        data.put(channel.getId(), updateChannel);
    }

    @Override
    public void deleteChannel(Channel channel) { // 채널 삭제
        validateChannel(channel);
        data.remove(channel.getId());
    }

    @Override
    public void enterChannel(User user, Channel channel) { // 사용자를 채널에 추가
        if (!channel.getUsers().contains(user)) {
            channel.addUser(user);
            user.addChannel(channel);
        }
    }

    @Override
    public void leaveChannel(User user, Channel channel) { // 사용자를 채널에서 제거
        if (channel.getUsers().contains(user)) {
            user.removeChannel(channel);
            channel.removeUser(user);
        }
    }

    @Override
    public void sendMessage(Channel channel, Message message) { // 채널에 메시지 추가
        if (channel.getMessages().contains(message)) {
            channel.addMessage(message);
            message.getSender().addChannel(channel);
        }
    }

    @Override
    public void deleteMessage(Channel channel, Message message) { // 메시지를 채널에서 제거
        if (channel.getMessages().contains(message)) {
            channel.removeMessage(message);
            message.getSender().removeChannel(channel);
        }
    }

    @Override
    public List<User> getUsers(Channel channel) { // 채널 내 사용자 목록 반환
        return new ArrayList<>(channel.getUsers());
    }

    @Override
    public List<Message> getMessages(Channel channel) { // 채널 내 메시지 목록 반환
        return new ArrayList<>(channel.getMessages());
    }

    public void validateChannel(Channel channel) { // 채널 존재 여부 검증
        if (!data.containsKey(channel.getId())) {
            throw new IllegalArgumentException("유효하지 않은 채널입니다.");
        }
    }
}

