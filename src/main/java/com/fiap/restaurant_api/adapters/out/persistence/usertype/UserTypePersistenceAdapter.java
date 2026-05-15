package com.fiap.restaurant_api.adapters.out.persistence.usertype.adapter;

import com.fiap.restaurant_api.adapters.out.persistence.usertype.entity.UserTypeEntity;
import com.fiap.restaurant_api.adapters.out.persistence.usertype.mapper.UserTypeEntityMapper;
import com.fiap.restaurant_api.adapters.out.persistence.usertype.repository.JpaUserTypeRepository;
import com.fiap.restaurant_api.domain.model.UserType;
import com.fiap.restaurant_api.domain.port.output.UserTypeRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserTypePersistenceAdapter
        implements UserTypeRepositoryPort {

    private final JpaUserTypeRepository repository;

    public UserTypePersistenceAdapter(
            JpaUserTypeRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public UserType save(UserType userType) {

        UserTypeEntity entity =
                UserTypeEntityMapper.toEntity(userType);

        UserTypeEntity saved =
                repository.save(entity);

        return UserTypeEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<UserType> findById(UUID id) {

        return repository.findById(id)
                .map(UserTypeEntityMapper::toDomain);
    }

    @Override
    public Optional<UserType> findByName(String name) {

        return repository.findByName(name)
                .map(UserTypeEntityMapper::toDomain);
    }

    @Override
    public List<UserType> findAll() {

        return repository.findAll()
                .stream()
                .map(UserTypeEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(UserType userType) {

        repository.delete(
                UserTypeEntityMapper.toEntity(userType)
        );
    }
}