package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class Channel extends BaseEntity {
    private final List<Integer> users;
    private String name;

    public Channel(String name) {
        super();
        this.users = new ArrayList<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
        this.setUpdatedAt();
    }

    public List<Integer> getUsers() {
        return users;
    }

    public void addUser(int userId) {
        users.add(userId);
        this.setUpdatedAt();
    }

    public void removeUser(int userId) {
        users.remove(userId);
        this.setUpdatedAt();
    }


}
