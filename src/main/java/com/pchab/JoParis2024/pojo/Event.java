package com.pchab.JoParis2024.pojo;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Event {

    /* Event ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* Event attributes */
    @NotBlank(message = "Title can't be null")
    private String title;

    /* Event description */
    @NotBlank(message = "Description can't be null")
    private String description;

    /* Event location */
    @Enumerated(EnumType.STRING)
    @NotNull(message = "City can't be null")
    private CityEnum city;

    /* Event stadium */
    @NotBlank(message = "Stadium can't be null")
    private String stadium;

    /* Event date */
    @NotNull(message = "Date can't be null")
    private LocalDateTime date;

    /* Event capacity */
    @NotNull(message = "Capacity can't be null")
    @Positive
    private Integer capacity;

    /* Event sport */
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Sport can't be null")
    private SportEnum sport;

    /* Event price */
    @NotNull(message = "Price can't be null")
    @Positive(message = "Price must be positive")
    private Float price;

    /* Tickets associated with the event    */
    @OneToMany(mappedBy = "event")
    @JsonIgnore
    private List<Ticket> tickets;

}
