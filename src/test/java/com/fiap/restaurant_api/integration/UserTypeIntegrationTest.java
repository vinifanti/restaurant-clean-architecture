package com.fiap.restaurant_api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.restaurant_api.application.usertype.dto.CreateUserTypeDto;
import com.fiap.restaurant_api.application.usertype.dto.UpdateUserTypeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserTypeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateUserType() throws Exception {
        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUserTypeDto("Cliente", "Tipo cliente"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Cliente"))
                .andExpect(jsonPath("$.description").value("Tipo cliente"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void shouldNotCreateDuplicateUserType() throws Exception {
        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUserTypeDto("Dono", "Dono de restaurante"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUserTypeDto("Dono", "Outro dono"))))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldListAllUserTypes() throws Exception {
        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUserTypeDto("Staff", "Funcionário"))))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/user-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldFindUserTypeById() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUserTypeDto("Manager", "Gerente"))))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/user-types/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Manager"));
    }

    @Test
    void shouldReturn404WhenUserTypeNotFound() throws Exception {
        mockMvc.perform(get("/user-types/9999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldUpdateUserType() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUserTypeDto("Cashier", "Caixa"))))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(put("/user-types/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateUserTypeDto("Caixa atualizado"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Caixa atualizado"));
    }

    @Test
    void shouldDeleteUserType() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUserTypeDto("Temp", "Temporário"))))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(delete("/user-types/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/user-types/{id}", id))
                .andExpect(status().is4xxClientError());
    }
}
