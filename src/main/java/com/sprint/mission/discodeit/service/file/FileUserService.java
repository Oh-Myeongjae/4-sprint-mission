package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private static final String FILE_PATH = "data/members.csv";
    private final Map<UUID, User> userData = new HashMap<>();

    @Override
    public User createUser(String nickName) {
        User newUser = new User(nickName);
        userData.put(newUser.getId(), newUser);

        saveUser();
        return newUser;
    }

    @Override
    public User getUser(User user) { // 특정 사용자 조회
        validateUser(user);
        return userData.get(user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        Map<UUID, User> users = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            users = (Map<UUID, User>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateNickname(User user, String nickName) { // 사용자 닉네임 변경
        validateUser(user);

        User findUser = userData.get(user.getId());
        findUser.updateNickName(nickName);
        userData.put(user.getId(), findUser);

        saveUser();
        return user;
    }

    @Override
    public void deleteUser(User user) { // 사용자 삭제
        validateUser(user);
        userData.remove(user.getId());
        user.getChannels().forEach(channel -> channel.removeUser(user));

        saveUser();
    }

    private void saveUser() {
        try {
            FileOutputStream fos = new FileOutputStream(FILE_PATH);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(userData);

        } catch (IOException e) {
            System.out.println("회원정보 저장에 실패했습니다.");
        }
    }

    public void validateUser(User user) { // 사용자 존재 여부 검증
        if (!userData.containsKey(user.getId())) {
            throw new NoSuchElementException("유효하지 않은 유저입니다.");
        }
    }
}
