package com.fiap.restaurant_api.adapters.persistence.user.repository;

import com.fiap.restaurant_api.adapters.persistence.user.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository
        extends JpaRepository<UserJpaEntity, UUID> {

    Optional<UserJpaEntity> findByEmail(String email);

    Optional<UserJpaEntity> findByLogin(String login);

    List<UserJpaEntity> findByNameContainingIgnoreCase(
            String name
    );
}