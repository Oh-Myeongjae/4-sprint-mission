package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {

    private final Map<UUID, User> userData;
    private static final String FILE_PATH = "data/members.csv";
    
    public FileUserRepository() {
        userData = initData();
    }

    public Map<UUID, User> initData() {
        Map<UUID, User> map = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            map = (Map<UUID, User>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("회원 초기화에 실패했습니다.");
        }

        return map;
    }

    @Override
    public void setData(User user) {
        userData.put(user.getId(), user);
        persist();
    }

    @Override
    public void setData() {
        persist();
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userData.values());
    }
    
    @Override
    public User getUser(User user) {
        return userData.get(user.getId());
    }
    
    @Override
    public void removeData(User user) {
        userData.remove(user.getId());
        persist();
    }

    private void persist() {
        try {
            FileOutputStream fos = new FileOutputStream(FILE_PATH);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(userData);

        } catch (IOException e) {
            System.out.println("회원 저장에 실패했습니다.");
        }
    }
}
