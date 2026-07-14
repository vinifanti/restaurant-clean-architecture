package com.fiap.restaurant_api.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

class MenuItemTest {

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        UserType userType = new UserType(1L, "Owner", null, LocalDateTime.now());
        User owner = new User(1L, "Mario", "mario@email.com", "mario", "password123", userType, LocalDateTime.now());
        restaurant = new Restaurant(1L, "Cantina", "Rua A, 1", "Italiana",
                LocalTime.of(11, 0), LocalTime.of(23, 0), owner);
    }

    @Test
    void shouldCreateMenuItemWithValidData() {
        MenuItem item = new MenuItem(1L, "Lasanha", "Lasanha bolonhesa",
                new BigDecimal("45.90"), false, "/img/lasanha.jpg", restaurant);

        assertThat(item.getId()).isEqualTo(1L);
        assertThat(item.getName()).isEqualTo("Lasanha");
        assertThat(item.getDescription()).isEqualTo("Lasanha bolonhesa");
        assertThat(item.getPrice()).isEqualByComparingTo("45.90");
        assertThat(item.isDineInOnly()).isFalse();
        assertThat(item.getRestaurant()).isEqualTo(restaurant);
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThatThrownBy(() ->
                new MenuItem(null, null, "desc", new BigDecimal("10.00"), false, null, restaurant))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name cannot be empty");
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        assertThatThrownBy(() ->
                new MenuItem(null, "  ", "desc", new BigDecimal("10.00"), false, null, restaurant))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowWhenDescriptionIsNull() {
        assertThatThrownBy(() ->
                new MenuItem(null, "Lasanha", null, new BigDecimal("10.00"), false, null, restaurant))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Description cannot be empty");
    }

    @Test
    void shouldThrowWhenDescriptionIsBlank() {
        assertThatThrownBy(() ->
                new MenuItem(null, "Lasanha", "  ", new BigDecimal("10.00"), false, null, restaurant))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowWhenPriceIsNull() {
        assertThatThrownBy(() ->
                new MenuItem(null, "Lasanha", "desc", null, false, null, restaurant))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Price must be greater than zero");
    }

    @Test
    void shouldThrowWhenPriceIsZero() {
        assertThatThrownBy(() ->
                new MenuItem(null, "Lasanha", "desc", BigDecimal.ZERO, false, null, restaurant))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Price must be greater than zero");
    }

    @Test
    void shouldThrowWhenPriceIsNegative() {
        assertThatThrownBy(() ->
                new MenuItem(null, "Lasanha", "desc", new BigDecimal("-1.00"), false, null, restaurant))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Price must be greater than zero");
    }

    @Test
    void shouldThrowWhenRestaurantIsNull() {
        assertThatThrownBy(() ->
                new MenuItem(null, "Lasanha", "desc", new BigDecimal("10.00"), false, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Restaurant is required");
    }

    @Test
    void shouldUpdateMenuItem() {
        MenuItem item = new MenuItem(1L, "Lasanha", "desc original",
                new BigDecimal("45.90"), false, "/img/lasanha.jpg", restaurant);

        item.update("Carbonara", "desc nova", new BigDecimal("55.00"), true, "/img/carbonara.jpg");

        assertThat(item.getName()).isEqualTo("Carbonara");
        assertThat(item.getDescription()).isEqualTo("desc nova");
        assertThat(item.getPrice()).isEqualByComparingTo("55.00");
        assertThat(item.isDineInOnly()).isTrue();
        assertThat(item.getImagePath()).isEqualTo("/img/carbonara.jpg");
    }

    @Test
    void shouldThrowOnUpdateWithInvalidPrice() {
        MenuItem item = new MenuItem(1L, "Lasanha", "desc",
                new BigDecimal("45.90"), false, null, restaurant);

        assertThatThrownBy(() ->
                item.update("Lasanha", "desc", BigDecimal.ZERO, false, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
