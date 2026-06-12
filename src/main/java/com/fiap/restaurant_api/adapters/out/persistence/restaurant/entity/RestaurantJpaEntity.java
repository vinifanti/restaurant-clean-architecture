package com.fiap.restaurant_api.adapters.out.persistence.restaurant.entity;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "restaurants")
public class RestaurantJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(name = "cuisine_type", nullable = false)
    private String cuisineType;

    @Column(name = "opening_hour", nullable = false)
    private LocalTime openingHour;

    @Column(name = "closing_hour", nullable = false)
    private LocalTime closingHour;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    public RestaurantJpaEntity() {
    }

    public RestaurantJpaEntity(
            Long id,
            String name,
            String address,
            String cuisineType,
            LocalTime openingHour,
            LocalTime closingHour,
            Long ownerId
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.ownerId = ownerId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCuisineType() { return cuisineType; }
    public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }

    public LocalTime getOpeningHour() { return openingHour; }
    public void setOpeningHour(LocalTime openingHour) { this.openingHour = openingHour; }

    public LocalTime getClosingHour() { return closingHour; }
    public void setClosingHour(LocalTime closingHour) { this.closingHour = closingHour; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
}