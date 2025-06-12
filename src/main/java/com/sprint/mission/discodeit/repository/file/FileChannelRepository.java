package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> channelData;
    private static final String FILE_PATH = "data/channels.csv";

    public FileChannelRepository() {
        channelData = initData();
    }

    private Map<UUID, Channel> initData() {
        Map<UUID, Channel> map = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            map = (Map<UUID, Channel>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("채널 초기화에 실패했습니다.");
        }

        return map;
    }
    @Override
    public void setData() {;
        persist();
    }

    @Override
    public void setData(Channel channel) {
        channelData.put(channel.getId(), channel);
        persist();
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(channelData.values());
    }

    @Override
    public Channel getChannel(Channel channel) {
        return channelData.get(channel.getId());
    }

    @Override
    public void removeData(Channel channel) {
        channelData.remove(channel.getId());
        persist();
    }

    private void persist() {
        try {
            FileOutputStream fos = new FileOutputStream(FILE_PATH);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(channelData);

        } catch (IOException e) {
            System.out.println("채널 저장에 실패했습니다.");
        }
    }
}
