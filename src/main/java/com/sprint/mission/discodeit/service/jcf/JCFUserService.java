package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final List<User> userList;
    public JCFUserService(){
        this.userList = new ArrayList<>();
    }

    @Override
    public User createUser(String username, String password, String email) {
        User newUser = new User(username, password, email);
        userList.add(newUser);
        System.out.println(username + "님 회원가입 완료되었습니다.");
        return newUser;
    }

    @Override
    public User findUserById(UUID id){
        return userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자 없음"));
    }

    @Override
    public List<User> findAllUsers(){
        return userList;
    }

    @Override
    public User updateUsername(UUID id, String newUsername){
        User targetUser = findUserById(id);
        targetUser.updateUsername(newUsername);
        return targetUser;
    }

    @Override
    public User updateEmail(UUID id, String newEmail){
        User targetUser = findUserById(id);
        targetUser.updateEmail(newEmail);
        return targetUser;
    }

    @Override
    public void deleteUser(UUID id){
        User targetUser = findUserById(id);
        userList.remove(targetUser);
        System.out.println(targetUser.getUsername() + "님 삭제 완료되었습니다");
    }

    @Override
    public User changePassword(UUID id, String newPassword) {
        User targetUser = findUserById(id);
        targetUser.updatePassword(newPassword);
        return targetUser;
    }
}
