package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker_IdOrderByStartDesc(Long bookerId);

    List<Booking> findAllByBooker_IdAndStartBeforeAndFinishAfterOrderByStartDesc(
            Long bookerId,
            LocalDateTime start,
            LocalDateTime finish
    );

    List<Booking> findAllByBooker_IdAndFinishBeforeOrderByStartDesc(Long bookerId, LocalDateTime finish);

    List<Booking> findAllByBooker_IdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime start);

    List<Booking> findAllByBooker_IdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    List<Booking> findAllByItem_Owner_IdOrderByStartDesc(Long ownerId);

    List<Booking> findAllByItem_Owner_IdAndStartBeforeAndFinishAfterOrderByStartDesc(
            Long ownerId,
            LocalDateTime start,
            LocalDateTime finish
    );

    List<Booking> findAllByItem_Owner_IdAndFinishBeforeOrderByStartDesc(Long ownerId, LocalDateTime finish);

    List<Booking> findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime start);

    List<Booking> findAllByItem_Owner_IdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);

    Optional<Booking> findFirstByItem_IdAndStatusAndFinishBeforeOrderByFinishDesc(
            Long itemId,
            BookingStatus status,
            LocalDateTime finish
    );

    Optional<Booking> findFirstByItem_IdAndStatusAndStartAfterOrderByStartAsc(
            Long itemId,
            BookingStatus status,
            LocalDateTime start
    );

    Optional<Booking> findFirstByItem_IdAndBooker_IdAndStatusOrderByFinishDesc(
            Long itemId, Long bookerId, BookingStatus status
    );

    @Query("select b " +
            "from Booking b " +
            "join fetch b.item i " +
            "join fetch b.booker u " +
            "where i.id in ?1 " +
            "and b.status = ?2 " +
            "and b.finish < ?3 " +
            "order by b.finish desc")

    List<Booking> findLastBookingsForItems(
            List<Long> itemIds,
            BookingStatus status,
            LocalDateTime now
    );

    @Query("select b " +
            "from Booking b " +
            "join fetch b.item i " +
            "join fetch b.booker u " +
            "where i.id in ?1 " +
            "and b.status = ?2 " +
            "and b.start > ?3 " +
            "order by b.start asc")

    List<Booking> findNextBookingsForItems(
            List<Long> itemIds,
            BookingStatus status,
            LocalDateTime now
    );
}
