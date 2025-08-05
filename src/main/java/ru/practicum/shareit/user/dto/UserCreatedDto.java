package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedDto {

    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String name;

    @Email(message = "Указан некорректный формат электронной почты")
    @NotBlank(message = "Электронная почта не может быть null")
    private String email;
}


