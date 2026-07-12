package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UserUpdateDto(
        @Pattern(regexp = "^(?!\\s*$).+", message = "name must not be blank")
        String name,
        @Email(message = "email must be valid")
        String email
) {
}
