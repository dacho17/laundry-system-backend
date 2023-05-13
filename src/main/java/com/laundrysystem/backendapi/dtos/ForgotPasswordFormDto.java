package com.laundrysystem.backendapi.dtos;

public class ForgotPasswordFormDto {
    private String email;

    public ForgotPasswordFormDto() {}

    public ForgotPasswordFormDto(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("Email = %s", email);
    }
}
