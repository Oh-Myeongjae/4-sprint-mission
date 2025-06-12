package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserService {
    User createUser(String nickName);

    User getUser(User user);
    List<User> getAllUsers();

    User updateNickname(User user, String nickName);

    void deleteUser(User user);

    void validateUser(User user);

}
