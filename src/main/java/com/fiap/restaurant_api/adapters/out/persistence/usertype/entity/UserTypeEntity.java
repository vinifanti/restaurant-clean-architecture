package com.fiap.restaurant_api.adapters.out.persistence.usertype.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_types")
public class UserTypeEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    public UserTypeEntity() {
    }

    public UserTypeEntity(
            UUID id,
            String name,
            String description,
            LocalDateTime lastUpdate
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lastUpdate = lastUpdate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}