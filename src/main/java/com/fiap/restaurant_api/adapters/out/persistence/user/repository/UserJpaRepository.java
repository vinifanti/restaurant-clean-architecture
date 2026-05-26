package com.fiap.restaurant_api.adapters.out.persistence.user.repository;

import com.fiap.restaurant_api.adapters.out.persistence.user.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository
        extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByEmail(String email);

    Optional<UserJpaEntity> findByLogin(String login);

    List<UserJpaEntity> findByNameContainingIgnoreCase(
            String name
    );
}

