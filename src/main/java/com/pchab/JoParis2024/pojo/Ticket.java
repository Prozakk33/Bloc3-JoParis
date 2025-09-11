package com.pchab.JoParis2024.pojo;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Ticket {

    // private User user;
    // private Event event;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticket_id;

    //@OneToMany
    private String userId;

    //@OneToMany
    private String eventId;

    private Date buyDate;

    private String ticketKey;

}
