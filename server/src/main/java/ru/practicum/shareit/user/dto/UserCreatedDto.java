package ru.practicum.shareit.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedDto {

    private String name;

    private String email;
}


