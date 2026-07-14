package com.fiap.restaurant_api.adapters.out.persistence.user;

import com.fiap.restaurant_api.adapters.out.persistence.user.entity.UserJpaEntity;
import com.fiap.restaurant_api.adapters.out.persistence.user.mapper.UserPersistenceMapper;
import com.fiap.restaurant_api.adapters.out.persistence.user.repository.UserJpaRepository;
import com.fiap.restaurant_api.domain.exception.BusinessException;
import com.fiap.restaurant_api.domain.model.User;
import com.fiap.restaurant_api.domain.model.UserType;
import com.fiap.restaurant_api.domain.port.output.UserRepositoryPort;
import com.fiap.restaurant_api.domain.port.output.UserTypeRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserPersistenceAdapter
        implements UserRepositoryPort {

    private final UserJpaRepository repository;
    private final UserTypeRepositoryPort userTypeRepository;

    public UserPersistenceAdapter(
            UserJpaRepository repository,
            UserTypeRepositoryPort userTypeRepository
    ) {
        this.repository = repository;
        this.userTypeRepository = userTypeRepository;
    }

    @Override
    public User save(User user) {

        UserJpaEntity entity =
                UserPersistenceMapper.toEntity(user);

        UserJpaEntity savedEntity =
                repository.save(entity);

        UserType userType = resolveUserType(savedEntity.getUserTypeId());

        return UserPersistenceMapper.toDomain(savedEntity, userType);
    }

    @Override
    public Optional<User> findById(Long id) {

        return repository.findById(id)
                .map(entity -> {
                    UserType userType = resolveUserType(entity.getUserTypeId());
                    return UserPersistenceMapper.toDomain(entity, userType);
                });
    }

    @Override
    public Optional<User> findByEmail(String email) {

        return repository.findByEmail(email)
                .map(entity -> {
                    UserType userType = resolveUserType(entity.getUserTypeId());
                    return UserPersistenceMapper.toDomain(entity, userType);
                });
    }

    @Override
    public Optional<User> findByLogin(String login) {

        return repository.findByLogin(login)
                .map(entity -> {
                    UserType userType = resolveUserType(entity.getUserTypeId());
                    return UserPersistenceMapper.toDomain(entity, userType);
                });
    }

    @Override
    public List<User> findByName(String name) {

        return repository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(entity -> {
                    UserType userType = resolveUserType(entity.getUserTypeId());
                    return UserPersistenceMapper.toDomain(entity, userType);
                })
                .toList();
    }

    @Override
    public List<User> findAll() {

        return repository.findAll()
                .stream()
                .map(entity -> {
                    UserType userType = resolveUserType(entity.getUserTypeId());
                    return UserPersistenceMapper.toDomain(entity, userType);
                })
                .toList();
    }

    @Override
    public void delete(User user) {

        repository.delete(
                UserPersistenceMapper.toEntity(user)
        );
    }

    private UserType resolveUserType(Long userTypeId) {

        return userTypeRepository.findById(userTypeId)
                .orElseThrow(() ->
                        new BusinessException("User type not found"));
    }
}

