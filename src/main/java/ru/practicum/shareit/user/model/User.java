package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Builder
@Data
public class User {

    @Positive(message = "Id пользователя не может быть отрицательным")
    private Integer id;
    @NotNull(message = "Имя пользователя не может быть null")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String name;
    @Email(message = "Указан некорректный формат электронной почты")
    @NotNull(message = "Электронная почта не может быть null")
    private String email;
}
