package com.fiap.restaurant_api.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

class RestaurantTest {

    private User owner;

    @BeforeEach
    void setUp() {
        UserType userType = new UserType(1L, "Owner", "Restaurant owner", LocalDateTime.now());
        owner = new User(1L, "Mario", "mario@email.com", "mario", "password123", userType, LocalDateTime.now());
    }

    @Test
    void shouldCreateRestaurantWithValidData() {
        Restaurant r = new Restaurant(1L, "Cantina", "Rua A, 1", "Italiana",
                LocalTime.of(11, 0), LocalTime.of(23, 0), owner);

        assertThat(r.getId()).isEqualTo(1L);
        assertThat(r.getName()).isEqualTo("Cantina");
        assertThat(r.getAddress()).isEqualTo("Rua A, 1");
        assertThat(r.getCuisineType()).isEqualTo("Italiana");
        assertThat(r.getOpeningHour()).isEqualTo(LocalTime.of(11, 0));
        assertThat(r.getClosingHour()).isEqualTo(LocalTime.of(23, 0));
        assertThat(r.getOwner()).isEqualTo(owner);
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThatThrownBy(() ->
                new Restaurant(null, null, "Rua A, 1", "Italiana",
                        LocalTime.of(11, 0), LocalTime.of(23, 0), owner))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name cannot be empty");
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        assertThatThrownBy(() ->
                new Restaurant(null, "  ", "Rua A, 1", "Italiana",
                        LocalTime.of(11, 0), LocalTime.of(23, 0), owner))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowWhenAddressIsNull() {
        assertThatThrownBy(() ->
                new Restaurant(null, "Cantina", null, "Italiana",
                        LocalTime.of(11, 0), LocalTime.of(23, 0), owner))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("address cannot be empty");
    }

    @Test
    void shouldThrowWhenCuisineTypeIsNull() {
        assertThatThrownBy(() ->
                new Restaurant(null, "Cantina", "Rua A, 1", null,
                        LocalTime.of(11, 0), LocalTime.of(23, 0), owner))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cuisine type cannot be empty");
    }

    @Test
    void shouldThrowWhenOpeningHourIsNull() {
        assertThatThrownBy(() ->
                new Restaurant(null, "Cantina", "Rua A, 1", "Italiana",
                        null, LocalTime.of(23, 0), owner))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("hours are required");
    }

    @Test
    void shouldThrowWhenClosingHourIsNull() {
        assertThatThrownBy(() ->
                new Restaurant(null, "Cantina", "Rua A, 1", "Italiana",
                        LocalTime.of(11, 0), null, owner))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("hours are required");
    }

    @Test
    void shouldThrowWhenOwnerIsNull() {
        assertThatThrownBy(() ->
                new Restaurant(null, "Cantina", "Rua A, 1", "Italiana",
                        LocalTime.of(11, 0), LocalTime.of(23, 0), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("owner is required");
    }

    @Test
    void shouldUpdateRestaurant() {
        Restaurant r = new Restaurant(1L, "Cantina", "Rua A, 1", "Italiana",
                LocalTime.of(11, 0), LocalTime.of(23, 0), owner);

        r.update("Trattoria", "Rua B, 2", "Brasileira",
                LocalTime.of(10, 0), LocalTime.of(22, 0));

        assertThat(r.getName()).isEqualTo("Trattoria");
        assertThat(r.getAddress()).isEqualTo("Rua B, 2");
        assertThat(r.getCuisineType()).isEqualTo("Brasileira");
        assertThat(r.getOpeningHour()).isEqualTo(LocalTime.of(10, 0));
        assertThat(r.getClosingHour()).isEqualTo(LocalTime.of(22, 0));
    }

    @Test
    void shouldThrowOnUpdateWithInvalidData() {
        Restaurant r = new Restaurant(1L, "Cantina", "Rua A, 1", "Italiana",
                LocalTime.of(11, 0), LocalTime.of(23, 0), owner);

        assertThatThrownBy(() ->
                r.update(null, "Rua B, 2", "Brasileira",
                        LocalTime.of(10, 0), LocalTime.of(22, 0)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
