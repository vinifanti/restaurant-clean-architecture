package com.fiap.restaurant_api.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserType {

    private UUID id;
    private String name;
    private String description;
    private LocalDateTime lastUpdate;

    public UserType(
            UUID id,
            String name,
            String description,
            LocalDateTime lastUpdate
    ) {

        validate(name);

        this.id = id;
        this.name = name;
        this.description = description;
        this.lastUpdate = lastUpdate;
    }

    private void validate(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "User type name cannot be empty"
            );
        }
    }

    public void changeDescription(String description) {

        this.description = description;
        this.lastUpdate = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
}