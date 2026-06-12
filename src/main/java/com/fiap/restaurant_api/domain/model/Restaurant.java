package com.fiap.restaurant_api.domain.model;

import java.time.LocalTime;

public class Restaurant {

    private Long id;
    private String name;
    private String address;
    private String cuisineType;
    private LocalTime openingHour;
    private LocalTime closingHour;
    private User owner;

    public Restaurant(
            Long id,
            String name,
            String address,
            String cuisineType,
            LocalTime openingHour,
            LocalTime closingHour,
            User owner
    ) {

        validate(
                name,
                address,
                cuisineType,
                openingHour,
                closingHour,
                owner
        );

        this.id = id;
        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.owner = owner;
    }

    private void validate(
            String name,
            String address,
            String cuisineType,
            LocalTime openingHour,
            LocalTime closingHour,
            User owner
    ) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Restaurant name cannot be empty"
            );
        }

        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException(
                    "Restaurant address cannot be empty"
            );
        }

        if (cuisineType == null || cuisineType.isBlank()) {
            throw new IllegalArgumentException(
                    "Cuisine type cannot be empty"
            );
        }

        if (openingHour == null || closingHour == null) {
            throw new IllegalArgumentException(
                    "Opening and closing hours are required"
            );
        }

        if (owner == null) {
            throw new IllegalArgumentException(
                    "Restaurant owner is required"
            );
        }
    }

    public void update(
            String name,
            String address,
            String cuisineType,
            LocalTime openingHour,
            LocalTime closingHour
    ) {

        validate(name, address, cuisineType, openingHour, closingHour, this.owner);

        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public LocalTime getOpeningHour() {
        return openingHour;
    }

    public LocalTime getClosingHour() {
        return closingHour;
    }

    public User getOwner() {
        return owner;
    }
}