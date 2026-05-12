package com.fiap.restaurant_api.domain.port.output;

import com.fiap.restaurant_api.domain.model.UserType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserTypeRepositoryPort {

    UserType save(UserType userType);

    Optional<UserType> findById(UUID id);

    Optional<UserType> findByName(String name);

    List<UserType> findAll();

    void delete(UserType userType);
}