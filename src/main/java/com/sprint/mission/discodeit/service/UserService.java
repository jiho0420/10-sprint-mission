package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(String username, String password, String email);

    User findUserById(UUID id);

    List<User> findAllUsers();

    User updateUsername(UUID id, String newUsername);

    User updateEmail(UUID id, String newEmail);

    User changePassword(UUID id, String newPassword);

    void deleteUser(UUID id);

}
