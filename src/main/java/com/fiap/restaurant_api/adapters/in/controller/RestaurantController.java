package com.fiap.restaurant_api.adapters.in.controller;

import com.fiap.restaurant_api.application.restaurant.dto.CreateRestaurantDto;
import com.fiap.restaurant_api.application.restaurant.dto.RestaurantResponseDto;
import com.fiap.restaurant_api.application.restaurant.dto.UpdateRestaurantDto;
import com.fiap.restaurant_api.domain.port.input.RestaurantUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantUseCase restaurantUseCase;

    public RestaurantController(RestaurantUseCase restaurantUseCase) {
        this.restaurantUseCase = restaurantUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponseDto create(
            @RequestBody @Valid CreateRestaurantDto dto
    ) {
        return restaurantUseCase.create(dto);
    }

    @GetMapping("/{id}")
    public RestaurantResponseDto findById(
            @PathVariable Long id
    ) {
        return restaurantUseCase.findById(id);
    }

    @GetMapping
    public List<RestaurantResponseDto> findAll() {
        return restaurantUseCase.findAll();
    }

    @GetMapping("/search")
    public List<RestaurantResponseDto> findByName(
            @RequestParam String name
    ) {
        return restaurantUseCase.findByName(name);
    }

    @GetMapping("/cuisine")
    public List<RestaurantResponseDto> findByCuisineType(
            @RequestParam String type
    ) {
        return restaurantUseCase.findByCuisineType(type);
    }

    @PutMapping("/{id}")
    public RestaurantResponseDto update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateRestaurantDto dto
    ) {
        return restaurantUseCase.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id
    ) {
        restaurantUseCase.delete(id);
    }
}