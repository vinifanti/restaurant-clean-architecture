package com.fiap.restaurant_api.domain.port.input;

import com.fiap.restaurant_api.application.user.dto.ChangePasswordDto;
import com.fiap.restaurant_api.application.user.dto.CreateUserDto;
import com.fiap.restaurant_api.application.user.dto.UpdateUserDto;
import com.fiap.restaurant_api.application.user.dto.UserResponseDto;

import java.util.List;

public interface UserUseCase {

    UserResponseDto create(CreateUserDto dto);

    UserResponseDto findById(Long id);

    List<UserResponseDto> findByName(String name);

    List<UserResponseDto> findAll();

    UserResponseDto update(
            Long id,
            UpdateUserDto dto
    );

    void changePassword(
            Long id,
            ChangePasswordDto dto
    );

    void delete(Long id);
}