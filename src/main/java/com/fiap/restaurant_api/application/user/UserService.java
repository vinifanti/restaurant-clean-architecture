package com.fiap.restaurant_api.application.user;

import com.fiap.restaurant_api.application.user.dto.ChangePasswordDto;
import com.fiap.restaurant_api.application.user.dto.CreateUserDto;
import com.fiap.restaurant_api.application.user.dto.UpdateUserDto;
import com.fiap.restaurant_api.application.user.dto.UserResponseDto;
import com.fiap.restaurant_api.application.user.mapper.UserMapper;
import com.fiap.restaurant_api.domain.exception.BusinessException;
import com.fiap.restaurant_api.domain.model.User;
import com.fiap.restaurant_api.domain.model.UserType;
import com.fiap.restaurant_api.domain.port.input.UserUseCase;
import com.fiap.restaurant_api.domain.port.output.PasswordEncoderPort;
import com.fiap.restaurant_api.domain.port.output.UserRepositoryPort;
import com.fiap.restaurant_api.domain.port.output.UserTypeRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService implements UserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserTypeRepositoryPort userTypeRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    public UserService(
            UserRepositoryPort userRepositoryPort,
            UserTypeRepositoryPort userTypeRepositoryPort,
            PasswordEncoderPort passwordEncoderPort
    ) {
        this.userRepositoryPort = userRepositoryPort;
        this.userTypeRepositoryPort = userTypeRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public UserResponseDto create(CreateUserDto dto) {

        validateEmail(dto.email());

        UserType userType = findUserType(dto.userTypeId());

        User user = new User(
                UUID.randomUUID(),
                dto.name(),
                dto.email(),
                dto.login(),
                passwordEncoderPort.encode(dto.password()),
                userType,
                LocalDateTime.now()
        );

        User savedUser = userRepositoryPort.save(user);

        return UserMapper.toResponse(savedUser);
    }

    @Override
    public UserResponseDto findById(UUID id) {

        User user = userRepositoryPort.findById(id)
                .orElseThrow(() ->
                        new BusinessException(
                                "User not found"
                        ));

        return UserMapper.toResponse(user);
    }

    @Override
    public List<UserResponseDto> findByName(String name) {

        return userRepositoryPort.findByName(name)
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public List<UserResponseDto> findAll() {

        return userRepositoryPort.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponseDto update(
            UUID id,
            UpdateUserDto dto
    ) {

        User user = userRepositoryPort.findById(id)
                .orElseThrow(() ->
                        new BusinessException(
                                "User not found"
                        ));

        validateUpdateEmail(
                id,
                dto.email()
        );

        user.updateProfile(
                dto.name(),
                dto.email(),
                dto.login()
        );

        User updatedUser =
                userRepositoryPort.save(user);

        return UserMapper.toResponse(updatedUser);
    }

    @Override
    public void changePassword(
            UUID id,
            ChangePasswordDto dto
    ) {

        User user = userRepositoryPort.findById(id)
                .orElseThrow(() ->
                        new BusinessException(
                                "User not found"
                        ));

        boolean passwordMatches =
                passwordEncoderPort.matches(
                        dto.currentPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {
            throw new BusinessException(
                    "Current password is invalid"
            );
        }

        user.changePassword(
                passwordEncoderPort.encode(
                        dto.newPassword()
                )
        );

        userRepositoryPort.save(user);
    }

    @Override
    public void delete(UUID id) {

        User user = userRepositoryPort.findById(id)
                .orElseThrow(() ->
                        new BusinessException(
                                "User not found"
                        ));

        userRepositoryPort.delete(user);
    }

    private void validateEmail(String email) {

        boolean emailAlreadyExists =
                userRepositoryPort.findByEmail(email)
                        .isPresent();

        if (emailAlreadyExists) {
            throw new BusinessException(
                    "Email already exists"
            );
        }
    }

    private void validateUpdateEmail(
            UUID userId,
            String email
    ) {

        userRepositoryPort.findByEmail(email)
                .ifPresent(user -> {

                    boolean isDifferentUser =
                            !user.getId().equals(userId);

                    if (isDifferentUser) {
                        throw new BusinessException(
                                "Email already exists"
                        );
                    }
                });
    }

    private UserType findUserType(UUID userTypeId) {

        return userTypeRepositoryPort.findById(userTypeId)
                .orElseThrow(() ->
                        new BusinessException(
                                "User type not found"
                        ));
    }
}