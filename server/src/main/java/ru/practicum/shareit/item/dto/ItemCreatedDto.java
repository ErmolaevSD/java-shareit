package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class ItemCreatedDto {

    private String name;

    private String description;

    private Boolean available;
}


