package com.pchab.JoParis2024.pojo;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Firstname can't be null")
    private String firstName;

    @NotNull(message = "Lastname can't be null")
    private String lastName;

    @NotNull(message = "Email can't be null")
    private String email;

    @NotNull(message = "Password can't be null")
    private String password;

    @NotNull(message = "userKey can't be null")
    private String userKey;

	@NotNull(message = "Role can't be null")
	private String role = "USER";

    @OneToMany(mappedBy = "user")
    private List<Ticket> tickets;

}
