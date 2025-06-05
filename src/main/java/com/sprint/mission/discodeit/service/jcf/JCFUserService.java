package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data; // 사용자 데이터를 저장하는 맵

    public JCFUserService() {
        data = new HashMap<>();
    }

    @Override
    public User createUser(String nickName) { // 새 사용자 생성 및 저장
        User user = new User(nickName);
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(User user) { // 특정 사용자 조회
        validateUser(user);
        return data.get(user.getId());
    }

    @Override
    public List<User> getAllUsers() { // 모든 사용자 목록 반환
        return new ArrayList<>(data.values());
    }

    @Override
    public User updateNickname(User user, String nickName) { // 사용자 닉네임 변경
        validateUser(user);
        User findUser = data.get(user.getId());
        findUser.updateNickName(nickName);
        return user;
    }

    @Override
    public void deleteUser(User user) { // 사용자 삭제
        validateUser(user);
        data.remove(user.getId());
        user.getChannels().forEach(channel -> channel.getUsers().remove(user));
    }

    @Override
    public void enterChannel(User user, Channel channel) { // 사용자가 채널에 참여
        user.addChannel(channel);
    }

    @Override
    public void leaveChannel(User user, Channel channel) { // 사용자가 채널에서 나감
        user.removeChannel(channel);
    }

    @Override
    public List<Channel> getChannels(User user) { // 사용자가 참여한 채널 목록 반환
        return new ArrayList<>(user.getChannels());
    }

    @Override
    public void sendMessage(User user, Message message) { // 사용자가 메시지 전송
        user.addMessage(message);
    }

    @Override
    public List<Message> getMessages(User user, Channel channel) { // 특정 채널 내 사용자의 메시지 목록 반환
        return new ArrayList<>(user.getMessages());
    }

    @Override
    public void deleteMessage(User user, Message message) { // 사용자의 메시지 삭제
        user.removeMessage(message);
    }

    public void validateUser(User user) { // 사용자 존재 여부 검증
        if (!data.containsKey(user.getId())) {
            throw new IllegalArgumentException("유효하지 않은 유저입니다.");
        }
    }
}

