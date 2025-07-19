package ru.practicum.shareit.request.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Entity
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Table(name = "request")
public class ItemRequest {

    @Positive
    @Id
    private Integer id;

    @Column(name = "description")
    private String description;

    @Column(name = "requestor_id")
    private Integer requestorId;
}