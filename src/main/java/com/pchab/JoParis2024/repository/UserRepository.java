package com.pchab.JoParis2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pchab.JoParis2024.pojo.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);

    @Query(value="SELECT u FROM User u WHERE u.email = ?1")
    public User findUserByEmail(String email);

}


