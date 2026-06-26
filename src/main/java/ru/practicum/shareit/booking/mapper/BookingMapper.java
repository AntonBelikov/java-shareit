package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Component
public class BookingMapper {
    public BookingDto toDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getFinish(),
                booking.getStatus(),
                new BookingItemDto(booking.getItem().getId(), booking.getItem().getName()),
                new BookingUserDto(booking.getBooker().getId())
        );
    }

    public BookingShortDto toShortDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        return new BookingShortDto(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getFinish()
        );
    }

    public Booking fromCreateDto(BookingCreateDto dto) {
        if (dto == null) {
            return null;
        }

        Booking booking = new Booking();
        booking.setStart(dto.getStart());
        booking.setFinish(dto.getFinish());
        return booking;
    }

    public List<BookingDto> toDtoList(List<Booking> bookings) {
        if (bookings == null) {
            return List.of();
        }

        return bookings.stream().map(this::toDto).toList();
    }
}
