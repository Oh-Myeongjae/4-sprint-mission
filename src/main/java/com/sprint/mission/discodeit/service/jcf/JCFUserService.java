package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    public JCFUserService() {
        data = new HashMap<>();
    }

    @Override
    public User createUser(String nickName) {
        User user = new User(nickName);
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(User user) {
        validateUser(user);
        return data.get(user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User updateNickname(User user, String nickName) {
        validateUser(user);
        User findUser = data.get(user.getId());
        findUser.updateNickName(nickName);
        return user;
    }

    @Override
    public void deleteUser(User user) {
        validateUser(user);
        data.remove(user.getId());
    }

    public void validateUser(User user) {
        if(!data.containsKey(user.getId())){
            throw new IllegalArgumentException("유효하지 않은 유저입니다.");
        }
    }
}
