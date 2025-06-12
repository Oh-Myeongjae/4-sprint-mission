package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

public class JCFUserRepository implements UserService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public JCFUserRepository(UserRepository userRepository, ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public User createUser(String nickName) {
        User newUser = new User(nickName);
        userRepository.setData(newUser);

        return newUser;
    }

    @Override
    public User getUser(User user) { // 특정 사용자 조회
        validateUser(user);
        return userRepository.getUser(user);
    }

    @Override
    public List<User> getAllUsers() {   // 모든 사용자 목록 반환
        return userRepository.getAllUsers();
    }

    @Override
    public User updateNickname(User user, String nickName) { // 사용자 닉네임 변경
        validateUser(user);

        User findUser = userRepository.getUser(user);
        findUser.updateNickName(nickName);

        userRepository.setData(findUser);
        return user;
    }

    @Override
    public void deleteUser(User user) { // 사용자 삭제
        validateUser(user);
        user.getChannels().forEach(channel -> channel.removeUser(user));

        userRepository.removeData(user);
        channelRepository.setData();
    }

    public void validateUser(User user) {
        if (userRepository.getUser(user) == null) {
            throw new NoSuchElementException("유효하지 않은 유저입니다.");
        }
    }
}
