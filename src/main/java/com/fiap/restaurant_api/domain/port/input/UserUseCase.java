package com.fiap.restaurant_api.domain.port.input;

import com.fiap.restaurant_api.application.user.dto.ChangePasswordDto;
import com.fiap.restaurant_api.application.user.dto.CreateUserDto;
import com.fiap.restaurant_api.application.user.dto.UpdateUserDto;
import com.fiap.restaurant_api.application.user.dto.UserResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserUseCase {

    UserResponseDto create(CreateUserDto dto);

    UserResponseDto findById(UUID id);

    List<UserResponseDto> findByName(String name);

    List<UserResponseDto> findAll();

    UserResponseDto update(
            UUID id,
            UpdateUserDto dto
    );

    void changePassword(
            UUID id,
            ChangePasswordDto dto
    );

    void delete(UUID id);
}