package com.fiap.restaurant_api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.restaurant_api.application.restaurant.dto.CreateRestaurantDto;
import com.fiap.restaurant_api.application.restaurant.dto.UpdateRestaurantDto;
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

import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RestaurantIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long ownerId;

    @BeforeEach
    void setUp() throws Exception {
        MvcResult userTypeResult = mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUserTypeDto("Dono", "Dono de restaurante"))))
                .andReturn();
        Long userTypeId = objectMapper.readTree(userTypeResult.getResponse().getContentAsString()).get("id").asLong();

        MvcResult userResult = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateUserDto("Mario Chef", "mario@chef.com", "mariochef", "password123", userTypeId))))
                .andReturn();
        ownerId = objectMapper.readTree(userResult.getResponse().getContentAsString()).get("id").asLong();
    }

    private MvcResult createRestaurant(String name, String cuisineType) throws Exception {
        return mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateRestaurantDto(name, "Rua das Flores, 123", cuisineType,
                                        LocalTime.of(11, 0), LocalTime.of(23, 0), ownerId))))
                .andReturn();
    }

    @Test
    void shouldCreateRestaurant() throws Exception {
        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateRestaurantDto("Cantina da Nonna", "Rua das Flores, 123", "Italiana",
                                        LocalTime.of(11, 0), LocalTime.of(23, 0), ownerId))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Cantina da Nonna"))
                .andExpect(jsonPath("$.cuisineType").value("Italiana"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void shouldNotCreateRestaurantWithInvalidOwner() throws Exception {
        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateRestaurantDto("X", "Rua Y", "Italiana",
                                        LocalTime.of(11, 0), LocalTime.of(23, 0), 9999L))))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldListAllRestaurants() throws Exception {
        createRestaurant("Burguer House", "Americana");

        mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldFindRestaurantById() throws Exception {
        MvcResult createResult = createRestaurant("Sushi Bar", "Japonesa");
        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/restaurants/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sushi Bar"))
                .andExpect(jsonPath("$.cuisineType").value("Japonesa"));
    }

    @Test
    void shouldReturn404WhenRestaurantNotFound() throws Exception {
        mockMvc.perform(get("/restaurants/9999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldSearchRestaurantByName() throws Exception {
        createRestaurant("Pizzaria Top", "Italiana");

        mockMvc.perform(get("/restaurants/search").param("name", "Pizzaria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldSearchRestaurantByCuisineType() throws Exception {
        createRestaurant("Taco Bell", "Mexicana");

        mockMvc.perform(get("/restaurants/cuisine").param("type", "Mexicana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldUpdateRestaurant() throws Exception {
        MvcResult createResult = createRestaurant("Old Name", "Italiana");
        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(put("/restaurants/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UpdateRestaurantDto("New Name", "Rua Nova, 99", "Francesa",
                                        LocalTime.of(12, 0), LocalTime.of(22, 0)))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.cuisineType").value("Francesa"));
    }

    @Test
    void shouldDeleteRestaurant() throws Exception {
        MvcResult createResult = createRestaurant("To Delete", "Italiana");
        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(delete("/restaurants/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/restaurants/{id}", id))
                .andExpect(status().is4xxClientError());
    }
}
