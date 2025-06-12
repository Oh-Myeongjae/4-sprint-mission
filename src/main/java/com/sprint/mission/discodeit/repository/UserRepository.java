package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserRepository {
    void setData();
    void setData(User user);
    List<User> getAllUsers();
    User getUser(User user);
    void removeData(User user);
}
