package com.laundrysystem.backendapi.dtos;

public class PasswordResetFormDto {
    private String username;
    private String password;

    public PasswordResetFormDto() {}

    public PasswordResetFormDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "PasswordResetFormDto [username=" + username + ", password=" + password + "]";
    }
}
