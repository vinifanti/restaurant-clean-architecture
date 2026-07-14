package com.fiap.restaurant_api.domain.model;

import com.fiap.restaurant_api.domain.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    private UserType userType;

    @BeforeEach
    void setUp() {
        userType = new UserType(1L, "Admin", "Admin role", LocalDateTime.now());
    }

    @Test
    void shouldCreateUserWithValidData() {
        User user = new User(1L, "John", "john@email.com", "john", "password123", userType, LocalDateTime.now());

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("John");
        assertThat(user.getEmail()).isEqualTo("john@email.com");
        assertThat(user.getLogin()).isEqualTo("john");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getUserType()).isEqualTo(userType);
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThatThrownBy(() ->
                new User(null, null, "john@email.com", "john", "password123", userType, LocalDateTime.now()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Name cannot be empty");
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        assertThatThrownBy(() ->
                new User(null, "  ", "john@email.com", "john", "password123", userType, LocalDateTime.now()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Name cannot be empty");
    }

    @Test
    void shouldThrowWhenEmailIsNull() {
        assertThatThrownBy(() ->
                new User(null, "John", null, "john", "password123", userType, LocalDateTime.now()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Email cannot be empty");
    }

    @Test
    void shouldThrowWhenLoginIsNull() {
        assertThatThrownBy(() ->
                new User(null, "John", "john@email.com", null, "password123", userType, LocalDateTime.now()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Login cannot be empty");
    }

    @Test
    void shouldThrowWhenPasswordIsTooShort() {
        assertThatThrownBy(() ->
                new User(null, "John", "john@email.com", "john", "short", userType, LocalDateTime.now()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("8 characters");
    }

    @Test
    void shouldThrowWhenPasswordIsNull() {
        assertThatThrownBy(() ->
                new User(null, "John", "john@email.com", "john", null, userType, LocalDateTime.now()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("8 characters");
    }

    @Test
    void shouldThrowWhenUserTypeIsNull() {
        assertThatThrownBy(() ->
                new User(null, "John", "john@email.com", "john", "password123", null, LocalDateTime.now()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("User type is required");
    }

    @Test
    void shouldUpdateProfile() {
        User user = new User(1L, "John", "john@email.com", "john", "password123", userType, LocalDateTime.now());
        user.updateProfile("Jane", "jane@email.com", "jane");

        assertThat(user.getName()).isEqualTo("Jane");
        assertThat(user.getEmail()).isEqualTo("jane@email.com");
        assertThat(user.getLogin()).isEqualTo("jane");
    }

    @Test
    void shouldThrowOnUpdateProfileWithBlankName() {
        User user = new User(1L, "John", "john@email.com", "john", "password123", userType, LocalDateTime.now());
        assertThatThrownBy(() -> user.updateProfile("  ", "jane@email.com", "jane"))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void shouldThrowOnUpdateProfileWithBlankEmail() {
        User user = new User(1L, "John", "john@email.com", "john", "password123", userType, LocalDateTime.now());
        assertThatThrownBy(() -> user.updateProfile("Jane", "", "jane"))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void shouldThrowOnUpdateProfileWithBlankLogin() {
        User user = new User(1L, "John", "john@email.com", "john", "password123", userType, LocalDateTime.now());
        assertThatThrownBy(() -> user.updateProfile("Jane", "jane@email.com", ""))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void shouldChangePassword() {
        User user = new User(1L, "John", "john@email.com", "john", "password123", userType, LocalDateTime.now());
        user.changePassword("newpassword99");
        assertThat(user.getPassword()).isEqualTo("newpassword99");
    }

    @Test
    void shouldThrowOnChangePasswordTooShort() {
        User user = new User(1L, "John", "john@email.com", "john", "password123", userType, LocalDateTime.now());
        assertThatThrownBy(() -> user.changePassword("short"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("8 characters");
    }

    @Test
    void shouldThrowOnChangePasswordNull() {
        User user = new User(1L, "John", "john@email.com", "john", "password123", userType, LocalDateTime.now());
        assertThatThrownBy(() -> user.changePassword(null))
                .isInstanceOf(ValidationException.class);
    }
}
