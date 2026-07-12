package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateDto(
        @NotBlank(message = "name must not be blank")
        String name,
        @Email(message = "email must be valid")
        String email
) {
}
