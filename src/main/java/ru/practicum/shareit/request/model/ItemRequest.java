package ru.practicum.shareit.request.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "request")
public class ItemRequest {

    @Id
    private Integer id;

    @Column(name = "description")
    private String description;

    @Column(name = "requestor_id")
    private Integer requestorId;
}