package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreatedDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static ru.practicum.shareit.booking.model.BookingStatus.REJECTED;
import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;


@Service
@RequiredArgsConstructor
public  class BookingServiceDb implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingMapper bookingMapper;
    private final ItemMapper itemMapper;

    @Override
    public BookingResponseDto createBooking(Integer ownerId,
                                            BookingCreatedDto bookingCreatedDto) {
        User user = userService.getUser(ownerId);
        ItemDto item = itemService.getItem(bookingCreatedDto.getItemId());
        if (item.getAvailable() == false) {
            throw new RuntimeException("");
        }
        Booking booking = bookingMapper.toDtoBooking(bookingCreatedDto);
        booking.setItem(itemMapper.toDtoItem(item));
        booking.setBooker(user);
        booking.setStatus(WAITING);
        bookingRepository.save(booking);
        return bookingMapper.toBookingResponseDto(booking);
    }

    @Override
    public BookingResponseDto getBooking(Integer ownerId, Integer bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new NotFoundException("Бронирования с id " + bookingId + " не найдено");
        }

        if (booking.get().getBooker().getId().equals(ownerId) || booking.get().getItem().getOwner().getId().equals(ownerId)) {
            return bookingMapper.toBookingResponseDto(booking.get());
        } else {
            throw new NotValidException("Бронирование может видеть только владелец или тот кто бронировал");
        }
    }

    @Override
    public BookingResponseDto approveBooking(Integer ownerId, Integer bookingId, String approved) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new NotFoundException("Бронирования с id " + bookingId + " не найдено");
        }

        if (booking.get().getBooker().getId().equals(ownerId) || booking.get().getItem().getOwner().getId().equals(ownerId)) {
            booking.get().setStatus(BookingStatus.findEnumByDescription(approved));
            return bookingMapper.toBookingResponseDto(bookingRepository.save(booking.get()));
        } else {
            throw new NotValidException("Подтвердить бронирование может только владелец");
        }
    }

    @Override
    public List<Booking> getAllBookingByUser(Integer ownerId, BookingState state) {
        userService.getUser(ownerId);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        LocalDateTime now = LocalDateTime.now();

        if (isNull(state)) {
            state = BookingState.ALL;
        }

        return switch (state) {
            case CURRENT -> bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(ownerId, now, now, sort);
            case PAST -> bookingRepository.findByBookerIdAndEndIsBefore(ownerId, now, sort);
            case FUTURE -> bookingRepository.findByBookerIdAndStartIsAfter(ownerId, now, sort);
            case WAITING -> bookingRepository.findByBookerIdAndStatus(ownerId, WAITING, sort);
            case REJECTED -> bookingRepository.findByBookerIdAndStatus(ownerId, REJECTED, sort);
            case ALL -> bookingRepository.findByBookerId(ownerId, sort);
        };

    }

    @Override
    public List<Booking> getAllBookingByOwner(Integer ownerId, BookingState state) {
        userService.getUser(ownerId);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        LocalDateTime now = LocalDateTime.now();

        if (isNull(state)) {
            state = BookingState.ALL;
        }

        return switch (state) {
            case CURRENT -> bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(ownerId, now, now, sort);
            case PAST -> bookingRepository.findByItemOwnerIdAndEndIsBefore(ownerId, now, sort);
            case FUTURE -> bookingRepository.findByItemOwnerIdAndStartIsAfter(ownerId, now, sort);
            case WAITING -> bookingRepository.findByItemOwnerIdAndStatus(ownerId, WAITING, sort);
            case REJECTED -> bookingRepository.findByItemOwnerIdAndStatus(ownerId, REJECTED, sort);
            case ALL -> bookingRepository.findByItemOwnerId(ownerId, sort);
        };
    }
}