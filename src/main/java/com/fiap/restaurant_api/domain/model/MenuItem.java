package com.fiap.restaurant_api.domain.model;

import java.math.BigDecimal;

public class MenuItem {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean dineInOnly;
    private String imagePath;
    private Restaurant restaurant;

    public MenuItem(
            Long id,
            String name,
            String description,
            BigDecimal price,
            boolean dineInOnly,
            String imagePath,
            Restaurant restaurant
    ) {

        validate(
                name,
                description,
                price,
                restaurant
        );

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dineInOnly = dineInOnly;
        this.imagePath = imagePath;
        this.restaurant = restaurant;
    }

    private void validate(
            String name,
            String description,
            BigDecimal price,
            Restaurant restaurant
    ) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Menu item name cannot be empty"
            );
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException(
                    "Description cannot be empty"
            );
        }

        if (price == null || price.doubleValue() <= 0) {
            throw new IllegalArgumentException(
                    "Price must be greater than zero"
            );
        }

        if (restaurant == null) {
            throw new IllegalArgumentException(
                    "Restaurant is required"
            );
        }
    }

    public void update(
            String name,
            String description,
            BigDecimal price,
            boolean dineInOnly,
            String imagePath
    ) {

        validate(name, description, price, this.restaurant);

        this.name = name;
        this.description = description;
        this.price = price;
        this.dineInOnly = dineInOnly;
        this.imagePath = imagePath;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isDineInOnly() {
        return dineInOnly;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}