
package com.fiap.restaurant_api.adapters.in.controller;

import com.fiap.restaurant_api.application.menuitem.dto.CreateMenuItemDto;
import com.fiap.restaurant_api.application.menuitem.dto.MenuItemResponseDto;
import com.fiap.restaurant_api.application.menuitem.dto.UpdateMenuItemDto;
import com.fiap.restaurant_api.domain.port.input.MenuItemUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu-items")
public class MenuItemController {

    private final MenuItemUseCase menuItemUseCase;

    public MenuItemController(MenuItemUseCase menuItemUseCase) {
        this.menuItemUseCase = menuItemUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponseDto create(
            @RequestBody @Valid CreateMenuItemDto dto
    ) {
        return menuItemUseCase.create(dto);
    }

    @GetMapping("/{id}")
    public MenuItemResponseDto findById(
            @PathVariable Long id
    ) {
        return menuItemUseCase.findById(id);
    }

    @GetMapping
    public List<MenuItemResponseDto> findAll() {
        return menuItemUseCase.findAll();
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<MenuItemResponseDto> findByRestaurant(
            @PathVariable Long restaurantId
    ) {
        return menuItemUseCase.findByRestaurant(restaurantId);
    }

    @PutMapping("/{id}")
    public MenuItemResponseDto update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateMenuItemDto dto
    ) {
        return menuItemUseCase.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id
    ) {
        menuItemUseCase.delete(id);
    }
}