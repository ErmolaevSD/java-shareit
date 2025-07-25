package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreatedDto {

    private String text;
}
