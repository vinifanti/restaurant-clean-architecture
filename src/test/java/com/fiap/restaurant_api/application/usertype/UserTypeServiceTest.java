package com.fiap.restaurant_api.application.usertype;

import com.fiap.restaurant_api.application.usertype.dto.CreateUserTypeDto;
import com.fiap.restaurant_api.application.usertype.dto.UpdateUserTypeDto;
import com.fiap.restaurant_api.application.usertype.dto.UserTypeResponseDto;
import com.fiap.restaurant_api.domain.exception.BusinessException;
import com.fiap.restaurant_api.domain.model.UserType;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTypeServiceTest {

    @Mock
    private UserTypeRepositoryPort repository;

    @InjectMocks
    private UserTypeService service;

    private UserType userType;

    @BeforeEach
    void setUp() {
        userType = new UserType(1L, "Admin", "Administrator", LocalDateTime.now());
    }

    @Test
    void shouldCreateUserType() {
        when(repository.findByName("Admin")).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(userType);

        UserTypeResponseDto result = service.create(new CreateUserTypeDto("Admin", "Administrator"));

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Admin");
        verify(repository).save(any(UserType.class));
    }

    @Test
    void shouldThrowWhenNameAlreadyExists() {
        when(repository.findByName("Admin")).thenReturn(Optional.of(userType));

        assertThatThrownBy(() -> service.create(new CreateUserTypeDto("Admin", "desc")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void shouldFindById() {
        when(repository.findById(1L)).thenReturn(Optional.of(userType));

        UserTypeResponseDto result = service.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Admin");
    }

    @Test
    void shouldThrowWhenNotFoundById() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void shouldFindAll() {
        when(repository.findAll()).thenReturn(List.of(userType));

        List<UserTypeResponseDto> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Admin");
    }

    @Test
    void shouldReturnEmptyListWhenNoUserTypes() {
        when(repository.findAll()).thenReturn(List.of());

        List<UserTypeResponseDto> result = service.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    void shouldUpdateUserType() {
        when(repository.findById(1L)).thenReturn(Optional.of(userType));
        when(repository.save(any())).thenReturn(userType);

        UserTypeResponseDto result = service.update(1L, new UpdateUserTypeDto("new description"));

        assertThat(result).isNotNull();
        verify(repository).save(userType);
    }

    @Test
    void shouldThrowWhenUpdatingNotFoundUserType() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, new UpdateUserTypeDto("desc")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void shouldDeleteUserType() {
        when(repository.findById(1L)).thenReturn(Optional.of(userType));

        service.delete(1L);

        verify(repository).delete(userType);
    }

    @Test
    void shouldThrowWhenDeletingNotFoundUserType() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("not found");
    }
}
