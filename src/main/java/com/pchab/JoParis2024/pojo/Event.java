package com.pchab.JoParis2024.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotBlank(message = "Title can't be null")
//    private String title;
    /*
    @NotBlank(message = "Description can't be null")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "City can't be null")
    private CityEnum city;

    @NotBlank(message = "Stadium can't be null")
    private String stadium;

    @NotNull(message = "Date can't be null")
    private LocalDateTime date;

    @NotNull(message = "Capacity can't be null")
    @Positive
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Sport can't be null")
    private SportEnum sport;

    @NotNull(message = "Price can't be null")
    @Positive(message = "Price must be positive")
    private Float price;
     */
}
