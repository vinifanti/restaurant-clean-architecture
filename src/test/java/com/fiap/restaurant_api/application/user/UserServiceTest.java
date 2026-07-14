package com.fiap.restaurant_api.application.user;

import com.fiap.restaurant_api.application.user.dto.*;
import com.fiap.restaurant_api.domain.exception.BusinessException;
import com.fiap.restaurant_api.domain.model.User;
import com.fiap.restaurant_api.domain.model.UserType;
import com.fiap.restaurant_api.domain.port.output.PasswordEncoderPort;
import com.fiap.restaurant_api.domain.port.output.UserRepositoryPort;
import com.fiap.restaurant_api.domain.port.output.UserTypeRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private UserTypeRepositoryPort userTypeRepositoryPort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private UserService service;

    private UserType userType;
    private User user;

    @BeforeEach
    void setUp() {
        userType = new UserType(1L, "Admin", "Administrator", LocalDateTime.now());
        user = new User(1L, "John", "john@email.com", "john", "encodedPass123", userType, LocalDateTime.now());
    }

    @Test
    void shouldCreateUser() {
        when(userRepositoryPort.findByEmail("john@email.com")).thenReturn(Optional.empty());
        when(userTypeRepositoryPort.findById(1L)).thenReturn(Optional.of(userType));
        when(passwordEncoderPort.encode("password123")).thenReturn("encodedPass123");
        when(userRepositoryPort.save(any())).thenReturn(user);

        UserResponseDto result = service.create(
                new CreateUserDto("John", "john@email.com", "john", "password123", 1L));

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("John");
        assertThat(result.email()).isEqualTo("john@email.com");
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        when(userRepositoryPort.findByEmail("john@email.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() ->
                service.create(new CreateUserDto("John", "john@email.com", "john", "password123", 1L)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Email already exists");
    }

    @Test
    void shouldThrowWhenUserTypeNotFound() {
        when(userRepositoryPort.findByEmail("john@email.com")).thenReturn(Optional.empty());
        when(userTypeRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.create(new CreateUserDto("John", "john@email.com", "john", "password123", 99L)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("User type not found");
    }

    @Test
    void shouldFindById() {
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDto result = service.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("John");
    }

    @Test
    void shouldThrowWhenUserNotFoundById() {
        when(userRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void shouldFindByName() {
        when(userRepositoryPort.findByName("John")).thenReturn(List.of(user));

        List<UserResponseDto> result = service.findByName("John");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("John");
    }

    @Test
    void shouldFindAll() {
        when(userRepositoryPort.findAll()).thenReturn(List.of(user));

        List<UserResponseDto> result = service.findAll();

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldUpdateUser() {
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));
        when(userRepositoryPort.findByEmail("jane@email.com")).thenReturn(Optional.empty());
        when(userRepositoryPort.save(any())).thenReturn(
                new User(1L, "Jane", "jane@email.com", "jane", "encodedPass123", userType, LocalDateTime.now()));

        UserResponseDto result = service.update(1L, new UpdateUserDto("Jane", "jane@email.com", "jane"));

        assertThat(result.name()).isEqualTo("Jane");
        assertThat(result.email()).isEqualTo("jane@email.com");
    }

    @Test
    void shouldThrowWhenUpdatingUserNotFound() {
        when(userRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, new UpdateUserDto("Jane", "jane@email.com", "jane")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void shouldThrowWhenUpdatingWithEmailAlreadyUsedByAnotherUser() {
        User otherUser = new User(2L, "Other", "john@email.com", "other", "pass12345", userType, LocalDateTime.now());
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));
        when(userRepositoryPort.findByEmail("john@email.com")).thenReturn(Optional.of(otherUser));

        assertThatThrownBy(() -> service.update(1L, new UpdateUserDto("John", "john@email.com", "john")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Email already exists");
    }

    @Test
    void shouldAllowUpdateWithSameEmail() {
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));
        when(userRepositoryPort.findByEmail("john@email.com")).thenReturn(Optional.of(user));
        when(userRepositoryPort.save(any())).thenReturn(user);

        assertThatCode(() -> service.update(1L, new UpdateUserDto("John Updated", "john@email.com", "john")))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldChangePassword() {
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoderPort.matches("currentPass", "encodedPass123")).thenReturn(true);
        when(passwordEncoderPort.encode("newPassword99")).thenReturn("encodedNewPass");
        when(userRepositoryPort.save(any())).thenReturn(user);

        assertThatCode(() ->
                service.changePassword(1L, new ChangePasswordDto("currentPass", "newPassword99")))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldThrowWhenChangingPasswordWithInvalidCurrentPassword() {
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoderPort.matches("wrongPass", "encodedPass123")).thenReturn(false);

        assertThatThrownBy(() ->
                service.changePassword(1L, new ChangePasswordDto("wrongPass", "newPassword99")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Current password is invalid");
    }

    @Test
    void shouldThrowWhenChangingPasswordUserNotFound() {
        when(userRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.changePassword(99L, new ChangePasswordDto("pass", "newPassword99")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void shouldDeleteUser() {
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));

        service.delete(1L);

        verify(userRepositoryPort).delete(user);
    }

    @Test
    void shouldThrowWhenDeletingUserNotFound() {
        when(userRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("User not found");
    }
}
