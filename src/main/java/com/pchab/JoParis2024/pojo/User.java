package com.pchab.JoParis2024.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
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

    @Column(unique = true)
    @NotNull(message = "Email can't be null")
    private String email;

    @NotNull(message = "Password can't be null")
    private String password;

    @NotNull(message = "userKey can't be null")
    private String userKey;

	@NotNull(message = "Role can't be null")
	private String role;

    public User() {
    }
    
    public User(String firstName, String lastName, String email, String password, String userKey) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userKey = userKey;
        this.role = "USER";
    }

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Ticket> tickets;

}
