package com.pchab.JoParis2024.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import com.pchab.JoParis2024.pojo.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);

    @Query(value="SELECT u FROM User u WHERE u.email = ?1")
    public Optional<User> findByEmail(String email);

}


