package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfter(Integer userId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findByBookerIdAndEndIsBefore(Integer userId, LocalDateTime end, Sort sort);

    List<Booking> findByBookerIdAndStartIsAfter(Integer userId, LocalDateTime end, Sort sort);

    List<Booking> findByBookerIdAndStatus(Integer userId, BookingStatus status, Sort sort);

    List<Booking> findByBookerId(Integer userId, Sort sort);

    List<Booking> findByItemOwnerIdAndEndIsBefore(Integer ownerId, LocalDateTime end, Sort sort);

    List<Booking> findByItemOwnerIdAndStartIsAfter(Integer ownerId, LocalDateTime start, Sort sort);

    List<Booking> findByItemOwnerIdAndStatus(Integer ownerId, BookingStatus status, Sort sort);

    List<Booking> findByItemOwnerId(Integer ownerId, Sort sort);

    List<Booking> findByItemId(Integer itemId);

}
