package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CommentWithBooking {

    private Integer id;
    private String text;
    private Integer item;
    private String authorName;
    private LocalDateTime created;
}
