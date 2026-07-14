package com.fiap.restaurant_api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.restaurant_api.application.user.dto.ChangePasswordDto;
import com.fiap.restaurant_api.application.user.dto.CreateUserDto;
import com.fiap.restaurant_api.application.user.dto.UpdateUserDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long userTypeId;

    @BeforeEach
    void setUp() throws Exception {
        MvcResult result = mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUserTypeDto("Cliente", "Tipo cliente"))))
                .andReturn();
        userTypeId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    private MvcResult createUser(String name, String email, String login) throws Exception {
        return mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateUserDto(name, email, login, "password123", userTypeId))))
                .andReturn();
    }

    @Test
    void shouldCreateUser() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateUserDto("João Silva", "joao@email.com", "joao", "password123", userTypeId))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@email.com"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void shouldNotCreateUserWithDuplicateEmail() throws Exception {
        createUser("João", "duplicado@email.com", "joao1");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateUserDto("Outro", "duplicado@email.com", "joao2", "password123", userTypeId))))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldListAllUsers() throws Exception {
        createUser("Maria", "maria@email.com", "maria");

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldFindUserById() throws Exception {
        MvcResult createResult = createUser("Pedro", "pedro@email.com", "pedro");
        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pedro"))
                .andExpect(jsonPath("$.email").value("pedro@email.com"));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(get("/users/9999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldSearchUserByName() throws Exception {
        createUser("Carlos Pereira", "carlos@email.com", "carlos");

        mockMvc.perform(get("/users/search").param("name", "Carlos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        MvcResult createResult = createUser("Ana", "ana@email.com", "ana");
        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(put("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UpdateUserDto("Ana Souza", "ana.souza@email.com", "ana.souza"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ana Souza"))
                .andExpect(jsonPath("$.email").value("ana.souza@email.com"));
    }

    @Test
    void shouldChangePassword() throws Exception {
        MvcResult createResult = createUser("Lucas", "lucas@email.com", "lucas");
        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(patch("/users/{id}/password", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new ChangePasswordDto("password123", "newpassword99"))))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotChangePasswordWithWrongCurrentPassword() throws Exception {
        MvcResult createResult = createUser("Felipe", "felipe@email.com", "felipe");
        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(patch("/users/{id}/password", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new ChangePasswordDto("wrongpassword", "newpassword99"))))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        MvcResult createResult = createUser("Temp", "temp@email.com", "temp");
        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().is4xxClientError());
    }
}
