package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String username;
    private String password;
    private String email;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void updateUsername(String newUsername){
        this.username = newUsername;
        this.setUpdatedAt(System.currentTimeMillis());
    }

    public void updateEmail(String newEmail){
        this.email = newEmail;
        this.setUpdatedAt(System.currentTimeMillis());
    }

    public void updatePassword(String newPassword){
        this.password = newPassword;
        this.setUpdatedAt(System.currentTimeMillis());
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
