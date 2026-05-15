package com.fiap.restaurant_api.application.usertype;

import com.fiap.restaurant_api.application.usertype.dto.*;
import com.fiap.restaurant_api.application.usertype.mapper.UserTypeMapper;
import com.fiap.restaurant_api.domain.exception.BusinessException;
import com.fiap.restaurant_api.domain.model.UserType;
import com.fiap.restaurant_api.domain.port.input.UserTypeUseCase;
import com.fiap.restaurant_api.domain.port.output.UserTypeRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserTypeService implements UserTypeUseCase {

    private final UserTypeRepositoryPort repository;

    public UserTypeService(
            UserTypeRepositoryPort repository
    ) {
        this.repository = repository;
    }

    @Override
    public UserTypeResponseDto create(CreateUserTypeDto dto) {

        validateName(dto.name());

        UserType userType = new UserType(
                UUID.randomUUID(),
                dto.name(),
                dto.description(),
                LocalDateTime.now()
        );

        UserType saved =
                repository.save(userType);

        return UserTypeMapper.toResponse(saved);
    }

    @Override
    public UserTypeResponseDto findById(UUID id) {

        UserType userType = repository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(
                                "User type not found"
                        ));

        return UserTypeMapper.toResponse(userType);
    }

    @Override
    public List<UserTypeResponseDto> findAll() {

        return repository.findAll()
                .stream()
                .map(UserTypeMapper::toResponse)
                .toList();
    }

    @Override
    public UserTypeResponseDto update(
            UUID id,
            UpdateUserTypeDto dto
    ) {

        UserType userType = repository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(
                                "User type not found"
                        ));

        userType.changeDescription(
                dto.description()
        );

        UserType updated =
                repository.save(userType);

        return UserTypeMapper.toResponse(updated);
    }

    @Override
    public void delete(UUID id) {

        UserType userType = repository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(
                                "User type not found"
                        ));

        repository.delete(userType);
    }

    private void validateName(String name) {

        boolean alreadyExists =
                repository.findByName(name)
                        .isPresent();

        if (alreadyExists) {
            throw new BusinessException(
                    "User type already exists"
            );
        }
    }
}
