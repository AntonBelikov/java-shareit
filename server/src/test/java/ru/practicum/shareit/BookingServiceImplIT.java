package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookingServiceImplIT {
    @Autowired
    BookingService bookingService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;

    @Test
    void createAndApprove_shouldPersistAndChangeStatus() {
        User owner = userRepository.save(user("Owner", "owner@test.com"));
        User booker = userRepository.save(user("Booker", "booker@test.com"));

        Item item = itemRepository.save(item("Drill", owner, true));

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingCreateDto createDto = new BookingCreateDto();
        createDto.setItemId(item.getId());
        createDto.setStart(start);
        createDto.setEnd(end);

        BookingDto created = bookingService.create(booker.getId(), createDto);
        assertNotNull(created.getId());
        assertEquals(BookingStatus.WAITING, created.getStatus());
        assertEquals(item.getId(), created.getItem().getId());
        assertEquals(booker.getId(), created.getBooker().getId());

        BookingDto approved = bookingService.approve(owner.getId(), created.getId(), true);
        assertEquals(BookingStatus.APPROVED, approved.getStatus());
    }

    @Test
    void findAllByOwner_shouldReturnBookingsWhereUserIsOwnerNotBooker() {
        User owner1 = userRepository.save(user("Owner1", "owner1@test.com"));
        User owner2 = userRepository.save(user("Owner2", "owner2@test.com"));
        User booker1 = userRepository.save(user("Booker1", "booker1@test.com"));

        Item itemOwnedByOwner1 = itemRepository.save(item("I1", owner1, true));
        Item itemOwnedByOwner2 = itemRepository.save(item("I2", owner2, true));

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingCreateDto dto1 = new BookingCreateDto();
        dto1.setItemId(itemOwnedByOwner1.getId());
        dto1.setStart(start);
        dto1.setEnd(end);
        BookingDto b1 = bookingService.create(booker1.getId(), dto1);
        bookingService.approve(owner1.getId(), b1.getId(), true);

        BookingCreateDto dto2 = new BookingCreateDto();
        dto2.setItemId(itemOwnedByOwner2.getId());
        dto2.setStart(start.plusDays(1));
        dto2.setEnd(end.plusDays(1));
        BookingDto b2 = bookingService.create(owner1.getId(), dto2);
        bookingService.approve(owner2.getId(), b2.getId(), true);

        List<BookingDto> owner1Bookings = bookingService.findAllByOwner(owner1.getId(), BookingState.ALL);

        assertTrue(owner1Bookings.stream().anyMatch(b -> b.getId().equals(b1.getId())),
                "Owner1 должен видеть бронирования своих вещей");
        assertTrue(owner1Bookings.stream().noneMatch(b -> b.getId().equals(b2.getId())),
                "Owner1 не должен видеть бронирования чужих вещей, даже если он booker");
    }

    private User user(String name, String email) {
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        return u;
    }

    private Item item(String name, User owner, boolean available) {
        Item i = new Item();
        i.setName(name);
        i.setDescription("desc");
        i.setAvailable(available);
        i.setOwner(owner);
        return i;
    }
}