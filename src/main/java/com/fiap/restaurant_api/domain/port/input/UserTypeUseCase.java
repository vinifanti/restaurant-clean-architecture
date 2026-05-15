package com.fiap.restaurant_api.domain.port.input;

import com.fiap.restaurant_api.application.usertype.dto.CreateUserTypeDto;
import com.fiap.restaurant_api.application.usertype.dto.UpdateUserTypeDto;
import com.fiap.restaurant_api.application.usertype.dto.UserTypeResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserTypeUseCase {

    UserTypeResponseDto create(CreateUserTypeDto dto);

    UserTypeResponseDto findById(UUID id);

    List<UserTypeResponseDto> findAll();

    UserTypeResponseDto update(
            UUID id,
            UpdateUserTypeDto dto
    );

    void delete(UUID id);
}
