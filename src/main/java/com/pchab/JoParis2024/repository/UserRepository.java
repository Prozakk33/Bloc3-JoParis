package com.pchab.JoParis2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pojo.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findUserById(Long id);
	
	
}
