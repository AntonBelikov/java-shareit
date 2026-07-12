package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record BookingCreateDto(
        @NotNull(message = "Start date cannot be null")
        LocalDateTime start,
        @NotNull(message = "End date cannot be null")
        LocalDateTime end,
        @NotNull(message = "Item ID cannot be null")
        Long itemId
) {
}
