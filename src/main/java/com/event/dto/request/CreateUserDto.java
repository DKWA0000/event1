package com.event.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class CreateUserDto {

    @NotNull
    private String name;

    @NotNull
    private String password;

    @Email
    private String email;

    @NotNull
    private int role;

    public CreateUserDto(String name, String password, String email, int role) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public int getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "CreateUserDto{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
