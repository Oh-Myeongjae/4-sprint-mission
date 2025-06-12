package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class FileChannelService implements ChannelService {

    private static final String FILE_PATH = "data/channels.csv";
    private final Map<UUID, Channel> channelData = new HashMap<>();

    @Override
    public Channel createChannel(User user,String name) {
        Channel newChannel = new Channel(user,name);
        channelData.put(newChannel.getId(), newChannel);
        user.addChannel(newChannel);

        saveChannel();
        return newChannel;
    }

    @Override
    public Channel getChannel(Channel channel) { // 특정 채널 조회
        validateChannel(channel);
        return channelData.get(channel.getId());
    }

    @Override
    public List<Channel> getAllChannel() { // 모든 채널 목록 반환
        return new ArrayList<>(channelData.values());
    }

    @Override
    public void updateChannel(Channel channel, String name) { // 채널 이름 변경
        validateChannel(channel);

        Channel updateChannel = channelData.get(channel.getId());
        updateChannel.updateName(name);

        channelData.put(channel.getId(), updateChannel);

        saveChannel();
    }

    @Override
    public void deleteChannel(Channel channel) { // 채널 삭제
        validateChannel(channel);
        channelData.remove(channel.getId());

        saveChannel();
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
    public List<User> getUsers(Channel channel) { // 채널 내 사용자 목록 반환
        return new ArrayList<>(channel.getUsers());
    }

    @Override
    public List<Message> getMessages(Channel channel) { // 채널 내 메시지 목록 반환
        return new ArrayList<>(channel.getMessages());
    }

    private void saveChannel() {
        try {
            FileOutputStream fos = new FileOutputStream(FILE_PATH);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(channelData);

        } catch (IOException e) {
            System.out.println("채널정보 저장에 실패했습니다.");
        }
    }

    public void validateChannel(Channel channel) { // 채널 존재 여부 검증
        if (!channelData.containsKey(channel.getId())) {
            throw new NoSuchElementException("유효하지 않은 채널입니다.");
        }
    }
}
