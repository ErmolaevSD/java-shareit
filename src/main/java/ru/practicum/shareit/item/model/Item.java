package ru.practicum.shareit.item.model;

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
public class Item {

    @Positive
    private Integer id;
    @NotBlank
    @NotNull
    private String name;
    private String description;
    @NotNull
    private Integer ownerId;
    @NotNull
    private Boolean available;
    private Integer requestId;
}
