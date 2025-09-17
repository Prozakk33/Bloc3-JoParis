package com.pchab.JoParis2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pchab.JoParis2024.pojo.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);

    //@Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmail(String email);

}


