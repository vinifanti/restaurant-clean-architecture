package com.fiap.restaurant_api.adapters.in.controller;

import com.fiap.restaurant_api.application.usertype.dto.CreateUserTypeDto;
import com.fiap.restaurant_api.application.usertype.dto.UpdateUserTypeDto;
import com.fiap.restaurant_api.application.usertype.dto.UserTypeResponseDto;
import com.fiap.restaurant_api.domain.port.input.UserTypeUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user-types")
public class UserTypeController {

    private final UserTypeUseCase useCase;

    public UserTypeController(
            UserTypeUseCase useCase
    ) {
        this.useCase = useCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserTypeResponseDto create(
            @RequestBody CreateUserTypeDto dto
    ) {

        return useCase.create(dto);
    }

    @GetMapping("/{id}")
    public UserTypeResponseDto findById(
            @PathVariable UUID id
    ) {

        return useCase.findById(id);
    }

    @GetMapping
    public List<UserTypeResponseDto> findAll() {

        return useCase.findAll();
    }

    @PutMapping("/{id}")
    public UserTypeResponseDto update(
            @PathVariable UUID id,
            @RequestBody UpdateUserTypeDto dto
    ) {

        return useCase.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id
    ) {

        useCase.delete(id);
    }
}