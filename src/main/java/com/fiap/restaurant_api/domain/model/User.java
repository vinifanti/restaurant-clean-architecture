package com.fiap.restaurant_api.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fiap.restaurant_api.domain.exception.ValidationException;

public class User {

    private UUID id;
    private String name;
    private String email;
    private String login;
    private String password;
    private UserType userType;
    private LocalDateTime lastUpdate;

    public User(
            UUID id,
            String name,
            String email,
            String login,
            String password,
            UserType userType,
            LocalDateTime lastUpdate
    ) {

        validate(
                name,
                email,
                login,
                password,
                userType
        );

        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.userType = userType;
        this.lastUpdate = lastUpdate;
    }

    public void updateProfile(
            String name,
            String email,
            String login
    ) {

        if (name == null || name.isBlank()) {
            throw new ValidationException(
                    "Name cannot be empty"
            );
        }

        if (email == null || email.isBlank()) {
            throw new ValidationException(
                    "Email cannot be empty"
            );
        }

        if (login == null || login.isBlank()) {
            throw new ValidationException(
                    "Login cannot be empty"
            );
        }

        this.name = name;
        this.email = email;
        this.login = login;
        this.lastUpdate = LocalDateTime.now();
    }

    public void changePassword(String newPassword) {

        if (newPassword == null
                || newPassword.length() < 8) {

            throw new ValidationException(
                    "Password must contain at least 8 characters"
            );
        }

        this.password = newPassword;
        this.lastUpdate = LocalDateTime.now();
    }

    private void validate(
            String name,
            String email,
            String login,
            String password,
            UserType userType
    ) {

        if (name == null || name.isBlank()) {
            throw new ValidationException(
                    "Name cannot be empty"
            );
        }

        if (email == null || email.isBlank()) {
            throw new ValidationException(
                    "Email cannot be empty"
            );
        }

        if (login == null || login.isBlank()) {
            throw new ValidationException(
                    "Login cannot be empty"
            );
        }

        if (password == null
                || password.length() < 8) {

            throw new ValidationException(
                    "Password must contain at least 8 characters"
            );
        }

        if (userType == null) {
            throw new ValidationException(
                    "User type is required"
            );
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public UserType getUserType() {
        return userType;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
}