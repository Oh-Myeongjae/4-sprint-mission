package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserService {
    User createUser(String nickName);

    User getUser(User user);
    List<User> getAllUsers();

    User updateNickname(User user, String nickName);

    void deleteUser(User user);

    void enterChannel(User user, Channel channel);
    void leaveChannel(User user, Channel channel);
    List<Channel> getChannels(User user);

    void sendMessage(User user, Message message);
    List<Message> getMessages(User user, Channel channel);
    void deleteMessage(User user, Channel channel, Message message);

}
