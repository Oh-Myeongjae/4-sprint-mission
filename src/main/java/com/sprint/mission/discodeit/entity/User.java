package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity{
    private String nickName;

    public User(String nickName) {
        super();
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
        this.setUpdatedAt();
    }
}
