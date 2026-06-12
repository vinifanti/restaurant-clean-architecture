
package com.fiap.restaurant_api.application.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record UpdateRestaurantDto(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Address is required")
        String address,

        @NotBlank(message = "Cuisine type is required")
        String cuisineType,

        @NotNull(message = "Opening hour is required")
        LocalTime openingHour,

        @NotNull(message = "Closing hour is required")
        LocalTime closingHour
) {
}