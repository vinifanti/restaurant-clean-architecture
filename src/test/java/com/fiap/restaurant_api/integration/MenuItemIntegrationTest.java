package com.fiap.restaurant_api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.restaurant_api.application.menuitem.dto.CreateMenuItemDto;
import com.fiap.restaurant_api.application.menuitem.dto.UpdateMenuItemDto;
import com.fiap.restaurant_api.application.restaurant.dto.CreateRestaurantDto;
import com.fiap.restaurant_api.application.user.dto.CreateUserDto;
import com.fiap.restaurant_api.application.usertype.dto.CreateUserTypeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MenuItemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long restaurantId;

    @BeforeEach
    void setUp() throws Exception {
        MvcResult userTypeResult = mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUserTypeDto("Dono", "Dono"))))
                .andReturn();
        Long userTypeId = objectMapper.readTree(userTypeResult.getResponse().getContentAsString()).get("id").asLong();

        MvcResult userResult = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateUserDto("Chef Master", "chef@email.com", "chefmaster", "password123", userTypeId))))
                .andReturn();
        Long ownerId = objectMapper.readTree(userResult.getResponse().getContentAsString()).get("id").asLong();

        MvcResult restaurantResult = mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateRestaurantDto("Cantina", "Rua A, 1", "Italiana",
                                        LocalTime.of(11, 0), LocalTime.of(23, 0), ownerId))))
                .andReturn();
        restaurantId = objectMapper.readTree(restaurantResult.getResponse().getContentAsString()).get("id").asLong();
    }

    private MvcResult createMenuItem(String name, String description, BigDecimal price) throws Exception {
        return mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateMenuItemDto(name, description, price, false, null, restaurantId))))
                .andReturn();
    }

    @Test
    void shouldCreateMenuItem() throws Exception {
        mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateMenuItemDto("Lasanha", "Lasanha bolonhesa",
                                        new BigDecimal("45.90"), false, "/img/lasanha.jpg", restaurantId))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Lasanha"))
                .andExpect(jsonPath("$.price").value(45.90))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void shouldNotCreateMenuItemWithInvalidRestaurant() throws Exception {
        mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateMenuItemDto("X", "desc", new BigDecimal("10.00"), false, null, 9999L))))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldListAllMenuItems() throws Exception {
        createMenuItem("Pizza", "Pizza margherita", new BigDecimal("38.00"));

        mockMvc.perform(get("/menu-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldFindMenuItemById() throws Exception {
        MvcResult createResult = createMenuItem("Risoto", "Risoto funghi", new BigDecimal("52.00"));
        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/menu-items/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Risoto"))
                .andExpect(jsonPath("$.description").value("Risoto funghi"));
    }

    @Test
    void shouldReturn404WhenMenuItemNotFound() throws Exception {
        mockMvc.perform(get("/menu-items/9999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldFindMenuItemsByRestaurant() throws Exception {
        createMenuItem("Tiramisu", "Sobremesa italiana", new BigDecimal("25.00"));

        mockMvc.perform(get("/menu-items/restaurant/{restaurantId}", restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldUpdateMenuItem() throws Exception {
        MvcResult createResult = createMenuItem("Old Name", "old desc", new BigDecimal("30.00"));
        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(put("/menu-items/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UpdateMenuItemDto("New Name", "new desc", new BigDecimal("35.00"), true, null))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.price").value(35.00))
                .andExpect(jsonPath("$.dineInOnly").value(true));
    }

    @Test
    void shouldDeleteMenuItem() throws Exception {
        MvcResult createResult = createMenuItem("To Delete", "item temporario", new BigDecimal("15.00"));
        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(delete("/menu-items/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/menu-items/{id}", id))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturn404WhenFindByNonExistentRestaurant() throws Exception {
        mockMvc.perform(get("/menu-items/restaurant/9999"))
                .andExpect(status().is4xxClientError());
    }
}
