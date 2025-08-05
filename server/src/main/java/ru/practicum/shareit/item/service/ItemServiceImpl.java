package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserService userService;
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;

    @Override
    public ItemDto createItem(Integer ownerId,
                              ItemCreatedDto itemDto) {
        User user = userService.getUser(ownerId);
        Item item = itemMapper.toCreatedDtoItem(itemDto, requestRepository);
        item.setOwner(user);

        if (!(itemDto.getRequestId() == null)) {
            Optional<ItemRequest> request = requestRepository.findById(itemDto.getRequestId());
            item.setRequest(request.get());
        }

        Item save = itemRepository.save(item);
        return itemMapper.toItemDto(save);
    }

    @Override
    public ItemDto getItem(Integer id) {
        Optional<Item> byId = itemRepository.findById(id);
        if (byId.isEmpty()) {
            throw new NotFoundException("Вещи с id " + id + " не найдено");
        }
        List<Comment> allByCommentItemId = commentRepository.findAllByItemId(id);
        ItemDto itemDto = itemMapper.toItemDto(byId.get());
        List<CommentDto> commentDtos = allByCommentItemId.stream().map(itemMapper::toCommentCommentDto).toList();
        itemDto.setComments(commentDtos);
        List<Booking> bookingList = bookingRepository.findByItemId(id);
        Booking last = bookingList.stream()
                .filter(booking -> booking.getEnd().toLocalDate().isBefore(LocalDate.now()))
                .max(Comparator.comparing(Booking::getStart)).orElse(null);

        Booking next = bookingList.stream()
                .filter(booking -> booking.getEnd().toLocalDate().isAfter(LocalDate.now()))
                .max(Comparator.comparing(Booking::getStart)).orElse(null);
        itemDto.setLastBooking(last);
        itemDto.setNextBooking(next);
        return itemDto;
    }

    @Override
    public List<ItemDto> getItemByUser(Integer id) {
        return itemRepository.findAllByOwnerId(id).stream().map(itemMapper::toItemDto).toList();
    }

    @Override
    public ItemDto updateItem(Integer ownerId,
                              Integer itemId,
                              ItemDto itemDto) {
        userService.getUser(ownerId);
        ItemDto findItemDto = getItem(itemId);
        Item item = itemMapper.toDtoItem(findItemDto);
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new NotValidException("Обновить вещь может только владелец");
        } else {
            Item item1 = itemMapper.updateItemFromDto(itemDto, item);
            return itemMapper.toItemDto(item1);
        }
    }

    @Override
    public List<ItemDto> searchItem(String search) {
        if (search.isBlank()) {
            return List.of();
        }
        return itemRepository.search(search).stream().map(itemMapper::toItemDto).toList();
    }

    @Override
    public CommentDto postCommentByItem(Integer ownerId, Integer itemId, CommentCreatedDto comment) {
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        LocalDateTime now = LocalDateTime.now();
        Optional<Item> byId = itemRepository.findById(itemId);
        if (byId.isEmpty()) {
            throw new NotFoundException("Вещи с id " + itemId + " не найдено");
        }
        List<Booking> allBookingByUser = bookingRepository.findByBookerIdAndEndIsBefore(ownerId, now, sort);

        boolean getBooking = allBookingByUser.stream().anyMatch(booking -> booking.getBooker().getId().equals(ownerId));

        if (!getBooking) {
            throw new NotValidException("Пользователь вещь не брал");
        }

        User user = userService.getUser(ownerId);
        Item item = itemRepository.findById(itemId).get();

        Comment createdComment = Comment.builder()
                .item(item)
                .text(comment.getText())
                .author(user)
                .created(LocalDateTime.now())
                .build();
        Comment save = commentRepository.save(createdComment);
        return itemMapper.toCommentCommentDto(save);
    }
}