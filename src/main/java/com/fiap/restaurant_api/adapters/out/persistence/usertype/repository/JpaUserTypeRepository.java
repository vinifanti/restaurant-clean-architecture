package com.fiap.restaurant_api.adapters.out.persistence.usertype.repository;

import com.fiap.restaurant_api.adapters.out.persistence.usertype.entity.UserTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserTypeRepository
        extends JpaRepository<UserTypeEntity, UUID> {

    Optional<UserTypeEntity> findByName(String name);
}