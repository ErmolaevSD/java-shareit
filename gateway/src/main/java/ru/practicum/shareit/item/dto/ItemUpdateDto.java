package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;

@Builder
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ItemUpdateDto {

    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
    private User owner;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<CommentDto> comments;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemUpdateDto that = (ItemUpdateDto) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(available, that.available) && Objects.equals(requestId, that.requestId) && Objects.equals(owner, that.owner) && Objects.equals(lastBooking, that.lastBooking) && Objects.equals(nextBooking, that.nextBooking) && Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, available, requestId, owner, lastBooking, nextBooking, comments);
    }
}