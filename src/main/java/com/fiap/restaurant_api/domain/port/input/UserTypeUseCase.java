package com.fiap.restaurant_api.domain.port.input;

import com.fiap.restaurant_api.application.usertype.dto.CreateUserTypeDto;
import com.fiap.restaurant_api.application.usertype.dto.UpdateUserTypeDto;
import com.fiap.restaurant_api.application.usertype.dto.UserTypeResponseDto;

import java.util.List;

public interface UserTypeUseCase {

    UserTypeResponseDto create(CreateUserTypeDto dto);

    UserTypeResponseDto findById(Long id);

    List<UserTypeResponseDto> findAll();

    UserTypeResponseDto update(
            Long id,
            UpdateUserTypeDto dto
    );

    void delete(Long id);
}
