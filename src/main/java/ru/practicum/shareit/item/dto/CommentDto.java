package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CommentDto {
    private Integer id;
    private String text;
   // private Item item;
    private String authorName;
    private LocalDateTime created;
}
