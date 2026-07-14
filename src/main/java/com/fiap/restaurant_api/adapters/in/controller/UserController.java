package com.fiap.restaurant_api.adapters.in.controller;

import com.fiap.restaurant_api.application.user.dto.ChangePasswordDto;
import com.fiap.restaurant_api.application.user.dto.CreateUserDto;
import com.fiap.restaurant_api.application.user.dto.UpdateUserDto;
import com.fiap.restaurant_api.application.user.dto.UserResponseDto;
import com.fiap.restaurant_api.domain.port.input.UserUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserUseCase userUseCase;

    public UserController(
            UserUseCase userUseCase
    ) {
        this.userUseCase = userUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto create(
            @RequestBody @Valid CreateUserDto dto
    ) {

        return userUseCase.create(dto);
    }

    @GetMapping("/{id}")
    public UserResponseDto findById(
            @PathVariable Long id
    ) {

        return userUseCase.findById(id);
    }

    @GetMapping
    public List<UserResponseDto> findAll() {

        return userUseCase.findAll();
    }

    @GetMapping("/search")
    public List<UserResponseDto> findByName(
            @RequestParam String name
    ) {

        return userUseCase.findByName(name);
    }

    @PutMapping("/{id}")
    public UserResponseDto update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserDto dto
    ) {

        return userUseCase.update(id, dto);
    }

    @PatchMapping("/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @PathVariable Long id,
            @RequestBody @Valid ChangePasswordDto dto
    ) {

        userUseCase.changePassword(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id
    ) {

        userUseCase.delete(id);
    }
}
