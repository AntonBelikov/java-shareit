package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingShortDto {
    @NotNull
    private Long id;
    @NotNull
    private Long bookerId;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime finish;
}
