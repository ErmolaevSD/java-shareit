package ru.practicum.shareit.request.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestCreatedDto {
    private String description;
}
