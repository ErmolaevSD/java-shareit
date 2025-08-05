package ru.practicum.shareit.item.dto;

import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCreatedDto {

    private String name;

    private String description;

    private Boolean available;

    private Integer requestId;
}


