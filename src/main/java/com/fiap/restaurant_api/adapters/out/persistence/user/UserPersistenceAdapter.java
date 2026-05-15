package com.fiap.restaurant_api.adapters.out.persistence.user;

import com.fiap.restaurant_api.adapters.out.persistence.user.entity.UserJpaEntity;
import com.fiap.restaurant_api.adapters.out.persistence.user.mapper.UserPersistenceMapper;
import com.fiap.restaurant_api.adapters.out.persistence.user.repository.UserJpaRepository;
import com.fiap.restaurant_api.domain.model.User;
import com.fiap.restaurant_api.domain.port.output.UserRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserPersistenceAdapter
        implements UserRepositoryPort {

    private final UserJpaRepository repository;

    public UserPersistenceAdapter(
            UserJpaRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {

        UserJpaEntity entity =
                UserPersistenceMapper.toEntity(user);

        UserJpaEntity savedEntity =
                repository.save(entity);

        return UserPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {

        return repository.findById(id)
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {

        return repository.findByEmail(email)
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByLogin(String login) {

        return repository.findByLogin(login)
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public List<User> findByName(String name) {

        return repository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(UserPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findAll() {

        return repository.findAll()
                .stream()
                .map(UserPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(User user) {

        repository.delete(
                UserPersistenceMapper.toEntity(user)
        );
    }
}