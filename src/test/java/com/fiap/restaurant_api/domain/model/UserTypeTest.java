package com.fiap.restaurant_api.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class UserTypeTest {

    @Test
    void shouldCreateUserTypeWithValidData() {
        UserType userType = new UserType(1L, "Admin", "Administrator role", LocalDateTime.now());

        assertThat(userType.getId()).isEqualTo(1L);
        assertThat(userType.getName()).isEqualTo("Admin");
        assertThat(userType.getDescription()).isEqualTo("Administrator role");
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThatThrownBy(() -> new UserType(1L, null, "desc", LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name cannot be empty");
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        assertThatThrownBy(() -> new UserType(1L, "  ", "desc", LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name cannot be empty");
    }

    @Test
    void shouldAllowNullDescription() {
        UserType userType = new UserType(1L, "Admin", null, LocalDateTime.now());
        assertThat(userType.getDescription()).isNull();
    }

    @Test
    void shouldChangeDescription() {
        UserType userType = new UserType(1L, "Admin", "old", LocalDateTime.now());
        userType.changeDescription("new description");
        assertThat(userType.getDescription()).isEqualTo("new description");
    }

    @Test
    void shouldUpdateLastUpdateOnChangeDescription() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        UserType userType = new UserType(1L, "Admin", "old", before);
        userType.changeDescription("new");
        assertThat(userType.getLastUpdate()).isAfter(before);
    }
}
