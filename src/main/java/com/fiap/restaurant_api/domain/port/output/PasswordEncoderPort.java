package com.fiap.restaurant_api.domain.port.output;

public interface PasswordEncoderPort {

    String encode(String rawPassword);

    boolean matches(
            String rawPassword,
            String encodedPassword
    );
}