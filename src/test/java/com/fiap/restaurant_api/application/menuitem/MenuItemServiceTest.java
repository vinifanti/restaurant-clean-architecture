package com.fiap.restaurant_api.application.menuitem;

import com.fiap.restaurant_api.application.menuitem.dto.CreateMenuItemDto;
import com.fiap.restaurant_api.application.menuitem.dto.MenuItemResponseDto;
import com.fiap.restaurant_api.application.menuitem.dto.UpdateMenuItemDto;
import com.fiap.restaurant_api.domain.exception.BusinessException;
import com.fiap.restaurant_api.domain.model.MenuItem;
import com.fiap.restaurant_api.domain.model.Restaurant;
import com.fiap.restaurant_api.domain.model.User;
import com.fiap.restaurant_api.domain.model.UserType;
import com.fiap.restaurant_api.domain.port.output.MenuItemRepositoryPort;
import com.fiap.restaurant_api.domain.port.output.RestaurantRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {

    @Mock
    private MenuItemRepositoryPort menuItemRepository;

    @Mock
    private RestaurantRepositoryPort restaurantRepository;

    @InjectMocks
    private MenuItemService service;

    private Restaurant restaurant;
    private MenuItem menuItem;

    @BeforeEach
    void setUp() {
        UserType userType = new UserType(1L, "Owner", null, LocalDateTime.now());
        User owner = new User(1L, "Mario", "mario@email.com", "mario", "password123", userType, LocalDateTime.now());
        restaurant = new Restaurant(1L, "Cantina", "Rua A, 1", "Italiana",
                LocalTime.of(11, 0), LocalTime.of(23, 0), owner);
        menuItem = new MenuItem(1L, "Lasanha", "Lasanha bolonhesa",
                new BigDecimal("45.90"), false, "/img/lasanha.jpg", restaurant);
    }

    @Test
    void shouldCreateMenuItem() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.save(any())).thenReturn(menuItem);

        MenuItemResponseDto result = service.create(
                new CreateMenuItemDto("Lasanha", "Lasanha bolonhesa",
                        new BigDecimal("45.90"), false, "/img/lasanha.jpg", 1L));

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Lasanha");
        assertThat(result.price()).isEqualByComparingTo("45.90");
    }

    @Test
    void shouldThrowWhenRestaurantNotFoundOnCreate() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.create(new CreateMenuItemDto("Lasanha", "desc",
                        new BigDecimal("10.00"), false, null, 99L)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Restaurant not found");
    }

    @Test
    void shouldFindById() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        MenuItemResponseDto result = service.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Lasanha");
    }

    @Test
    void shouldThrowWhenMenuItemNotFoundById() {
        when(menuItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Menu item not found");
    }

    @Test
    void shouldFindAll() {
        when(menuItemRepository.findAll()).thenReturn(List.of(menuItem));

        List<MenuItemResponseDto> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Lasanha");
    }

    @Test
    void shouldFindByRestaurant() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.findByRestaurantId(1L)).thenReturn(List.of(menuItem));

        List<MenuItemResponseDto> result = service.findByRestaurant(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Lasanha");
    }

    @Test
    void shouldThrowWhenFindByRestaurantNotFound() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByRestaurant(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Restaurant not found");
    }

    @Test
    void shouldUpdateMenuItem() {
        MenuItem updated = new MenuItem(1L, "Carbonara", "nova desc",
                new BigDecimal("55.00"), true, "/img/carbonara.jpg", restaurant);
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(menuItemRepository.save(any())).thenReturn(updated);

        MenuItemResponseDto result = service.update(1L,
                new UpdateMenuItemDto("Carbonara", "nova desc",
                        new BigDecimal("55.00"), true, "/img/carbonara.jpg"));

        assertThat(result.name()).isEqualTo("Carbonara");
        assertThat(result.price()).isEqualByComparingTo("55.00");
    }

    @Test
    void shouldThrowWhenUpdatingMenuItemNotFound() {
        when(menuItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.update(99L, new UpdateMenuItemDto("X", "desc", new BigDecimal("10.00"), false, null)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Menu item not found");
    }

    @Test
    void shouldDeleteMenuItem() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        service.delete(1L);

        verify(menuItemRepository).delete(menuItem);
    }

    @Test
    void shouldThrowWhenDeletingMenuItemNotFound() {
        when(menuItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Menu item not found");
    }
}
