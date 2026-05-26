package com.fiap.restaurant_api.domain.port.output;

import com.fiap.restaurant_api.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);

    List<User> findByName(String name);

    List<User> findAll();

    void delete(User user);
}